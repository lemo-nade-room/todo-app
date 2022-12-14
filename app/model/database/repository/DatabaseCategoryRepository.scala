package model.database.repository

import model.content.CategoryContent
import model.database.model.TodoCategoryModel
import model.database.{Connection, Table}
import model.repository.CategoryRepository
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class DatabaseCategoryRepository() extends CategoryRepository {
  /**
   * 全てのカテゴリ
   *
   * @return カテゴリは名前順に、todosは更新日順に（新しいもの順に）並ぶ
   */
  override def all(): Future[Seq[CategoryContent.View]] = {
    for (
      todos <- Connection.db.run(
        Table.todoCategories
          .joinLeft(Table.todos)
          .on(_.id === _.categoryId)
          .sortBy(_._1.name)
          .result
      )
    ) yield {
      todos.groupBy(_._1).map { case (category, todos) =>
        CategoryContent.View(
          category.id,
          category.name,
          category.slug,
          category.color,
          todos.map(_._2)
            .collect { case Some(value) => value }
            .sortWith((a, b) => a.updatedAt.after(b.updatedAt))
            .map(_.convertView)
        )
      }.toSeq
    }
  }

  override def create(category: CategoryContent.Create): Future[Long] = Connection.db.run {
    Table.todoCategories returning Table.todoCategories.map(_.id) += TodoCategoryModel(0, category.name, category.slug, category.color)
  }

  override def update(updateCategory: CategoryContent.Update): Future[Unit] = Connection.db.run {
    Table.todoCategories
      .filter(_.id === updateCategory.id)
      .map(category => (category.name, category.slug, category.color))
      .update((updateCategory.name, updateCategory.slug, updateCategory.color))
      .map(_ => Unit)
  }

  override def delete(categoryId: Long): Future[Unit] = {
    val todosDeleteQuery = Table.todos.filter(_.categoryId === categoryId).delete
    val categoryDeleteQuery = Table.todoCategories.filter(_.id === categoryId).delete
    Connection.db.run((todosDeleteQuery andThen categoryDeleteQuery).transactionally)
      .map(_ => Unit)
  }
}
