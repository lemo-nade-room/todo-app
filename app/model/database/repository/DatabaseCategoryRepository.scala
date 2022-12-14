package model.database.repository

import model.content.CategoryContent
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
  override def all(): Future[Seq[CategoryContent]] = {
    for (
      todos <- Connection.db.run(
        Table.todoCategories
          .join(Table.todos)
          .on(_.id === _.categoryId)
          .sortBy(_._1.name)
          .result
      )
    ) yield {
      todos
        .groupBy(_._1)
        .map { case (category, todos) => CategoryContent(
          category.id,
          category.name,
          category.slug,
          category.color,
          todos
            .sortWith((a, b) => a._2.updatedAt.after(b._2.updatedAt))
            .map(_._2.convertView)
        )}
        .toSeq
    }
  }
}
