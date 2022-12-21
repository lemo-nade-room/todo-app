package model.database.repository

import ixias.persistence.SlickRepository
import model.database.ixiasmodel.TodoCategoryModel
import model.database.{Connection, Table}
import model.entity.todo.TodoCategory
import model.entity.todo.category.{CategoryColor, CategoryID, CategoryName, CategorySlug}
import model.repository.CategoryRepository
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class DatabaseCategoryRepository[P <: JdbcProfile]()(implicit val driver: P)
  extends CategoryRepository
    with SlickRepository[TodoCategoryModel.Id, TodoCategoryModel, P]
    with SlickResourceProvider
    {

  override def all(): Future[Seq[TodoCategory]] = {
    Connection.db.run(Table.todoCategories.result)
      .map(_.map(_.category))
  }

  override def create(name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[TodoCategory] = {

    val a = TodoCategoryModel(name.name, slug.slug, color.color).toE

//    val newCategoryModel = TodoCategoryModel.make(name, slug, color)
//    val insertQuery = Table.todoCategories returning Table.todoCategories.map(_.id) += newCategoryModel
//    for {
//      id <- Connection.db.run(insertQuery)
//      createdModel <- Connection.db.run(Table.todoCategories.filter(_.id === id).result.head)
//    } yield createdModel.category
  }

  override def update(id: CategoryID, name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[Unit] = Connection.db.run {
    Table.todoCategories
      .filter(_.id === id.id)
      .map(model => (model.name, model.slug, model.color))
      .update((name.name, slug.slug, color.color))
      .map(_ => Unit)
  }

  override def delete(id: CategoryID): Future[Unit] = {
    val todosDeleteQuery = Table.todos.filter(_.categoryId === id.id).delete
    val categoryDeleteQuery = Table.todoCategories.filter(_.id === id.id).delete
    Connection.db.run((todosDeleteQuery andThen categoryDeleteQuery).transactionally)
      .map(_ => Unit)
  }

  override def find(id: CategoryID): Future[Option[TodoCategory]] = Connection.db.run {
    Table.todoCategories.filter(_.id === id.id).result.headOption
  }.map(_.map(_.category))
}
