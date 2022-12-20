package model.database.repository

import model.database.model.TodoCategoryModel
import model.database.{Connection, Table}
import model.entity.todo.TodoCategory
import model.entity.todo.category.{CategoryColor, CategoryID, CategoryName, CategorySlug}
import model.repository.CategoryRepository
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class DatabaseCategoryRepository() extends CategoryRepository {

  override def all(): Future[Seq[TodoCategory]] = {
    Connection.db.run(Table.todoCategories.result)
      .map(_.map(_.category))
  }

  override def create(name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[TodoCategory] = {
    val newCategoryModel = TodoCategoryModel.make(name, slug, color)
    val insertQuery = Table.todoCategories returning Table.todoCategories.map(_.id) += newCategoryModel
    for {
      id <- Connection.db.run(insertQuery)
      createdModel <- Connection.db.run(Table.todoCategories.filter(_.id === id).result.head)
    } yield createdModel.category
  }

  override def update(category: TodoCategory): Future[Unit] = Connection.db.run {
    Table.todoCategories
      .filter(_.id === category.id.id)
      .map(model => (model.name, model.slug, model.color))
      .update((category.name.name, category.slug.slug, category.color.color))
      .map(_ => Unit)
  }

  override def delete(id: CategoryID): Future[Unit] = {
    val todosDeleteQuery = Table.todos.filter(_.categoryId === id.id).delete
    val categoryDeleteQuery = Table.todoCategories.filter(_.id === id.id).delete
    Connection.db.run((todosDeleteQuery andThen categoryDeleteQuery).transactionally)
      .map(_ => Unit)
  }
}
