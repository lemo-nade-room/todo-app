package model.database.ixiasrepository

import com.google.inject.Inject
import ixias.persistence.SlickRepository
import model.database.SlickResourceProvider
import model.database.ixiasmodel.TodoCategoryModel
import model.entity.todo.TodoCategory
import model.entity.todo.category.{CategoryColor, CategoryID, CategoryName, CategorySlug}
import model.repository.CategoryRepository
import slick.jdbc.JdbcProfile
import scala.concurrent.Future

case class IxiasCategoryRepository[P <: JdbcProfile] @Inject()(implicit val driver: P)
  extends CategoryRepository
    with SlickRepository[TodoCategoryModel.Id, TodoCategoryModel, P]
    with SlickResourceProvider[P] {

  import api._

  def all(): Future[Seq[TodoCategory]] = {
    RunDBAction(TodoCategoryTable, "slave") {
      _.result
    } {
      _.map(_.category)
    }
  }

  def create(name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[CategoryID] = {
    val newCategoryModel = TodoCategoryModel.build(name, slug, color)
    for (id <- add(newCategoryModel)) yield CategoryID(id.longValue())
  }

  def update(id: CategoryID, name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[Unit] = {
    val newCategory = TodoCategoryModel.build(id, name, slug, color)
    update(newCategory).map(_ => Unit)
  }

  def delete(id: CategoryID): Future[Unit] = {
    DBAction(TodoCategoryTable, "master") { case (db, category) =>
      DBAction(TodoTable, "master") { case (_, todo) =>
        val categoryModelID = TodoCategoryModel.id(id)
        val todosDeleteQuery = todo.filter(_.categoryId === categoryModelID).delete
        val categoryDeleteQuery = category.filter(_.id === categoryModelID).delete
        db.run((todosDeleteQuery andThen categoryDeleteQuery).transactionally)
      }
    }.map(_ => Unit)
  }

  def find(id: CategoryID): Future[Option[TodoCategory]] = {
    get(TodoCategoryModel.id(id)).map(_.map(_.v.category))
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
