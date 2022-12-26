package model.database.ixiasrepository

import ixias.persistence.SlickRepository
import model.database.SlickResourceProvider
import model.database.ixiasmodel.TodoCategoryModel
import model.entity.todo.TodoCategory
import model.entity.todo.category.{CategoryColor, CategoryID, CategoryName, CategorySlug}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

case class IxiasCategoryRepository[P <: JdbcProfile]()(implicit val driver: P)
    extends SlickRepository[TodoCategoryModel.Id, TodoCategoryModel, P]
    with SlickResourceProvider[P] {

  import api._

  def all(): Future[Seq[TodoCategory]] = {
    RunDBAction(TodoCategoryTable, "slave") {
      _.result
    } {
      _.map(_.category)
    }
  }

  def create(name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[TodoCategory] = {
    val newCategoryModel = TodoCategoryModel(Option.empty, name.value, slug.value, color.value)
    for {
      categoryId <- RunDBAction(TodoCategoryTable, "master") { t => t returning t.map(_.id) += newCategoryModel }
      category <- RunDBAction(TodoCategoryTable, "slave") {
        _.filter(_.id === categoryId).result.head
      } {
        _.category
      }
    } yield category
  }

  def update(id: CategoryID, name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[Unit] = {
    RunDBAction(TodoCategoryTable, "master") {
      _.filter(_.id === TodoCategoryModel.Id(id.value.asInstanceOf[TodoCategoryModel.Id.U]))
        .map(model => (model.name, model.slug, model.color))
        .update((name.value, slug.value, color.value))
    }.map(_ => Unit)
  }

  def delete(id: CategoryID): Future[Unit] = {
    DBAction(TodoCategoryTable, "master") { case (db, category) =>
      DBAction(TodoTable, "master") { case (_, todo) =>
        val categoryModelID = TodoCategoryModel.Id(id.value.asInstanceOf[TodoCategoryModel.Id.U])
        val todosDeleteQuery = todo.filter(_.categoryId === categoryModelID).delete
        val categoryDeleteQuery = category.filter(_.id === categoryModelID).delete
        db.run((todosDeleteQuery andThen categoryDeleteQuery).transactionally)
      }
    }.map(_ => Unit)
  }

  def find(id: CategoryID): Future[Option[TodoCategory]] = {
    RunDBAction(TodoCategoryTable, "slave") {
      _.filter(_.id === TodoCategoryModel.Id(id.value.asInstanceOf[TodoCategoryModel.Id.U]))
        .result
        .headOption
    } {
      _.map(_.category)
    }
  }

  override def get(id: Id): Future[Option[EntityEmbeddedId]] = {
    RunDBAction(TodoCategoryTable, "slave") { t =>
      t.filter(_.id === id).result.headOption
    }
  }

  override def add(entity: EntityWithNoId): Future[Id] = {
    RunDBAction(TodoCategoryTable, "master") { t =>
      t returning t.map(_.id) += entity.v
    }.map(_.asInstanceOf[Id])
  }

  override def update(entity: EntityEmbeddedId): Future[Option[EntityEmbeddedId]] = {
    RunDBAction(TodoCategoryTable, "master") { t =>
      t.filter(_.id === entity.id)
        .update(entity.v)
    }
    RunDBAction(TodoCategoryTable, "slave") { t =>
      t.filter(_.id === entity.id).result.headOption
    }
  }

  def remove(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoCategoryTable) { slick =>
      val row = slick.filter(_.id === id)
      for {
        old <- row.result.headOption
        _ <- old match {
          case None => DBIO.successful(0)
          case Some(_) => row.delete
        }
      } yield old
    }
}
