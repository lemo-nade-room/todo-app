package model.database.ixiasrepository

import ixias.persistence.SlickRepository
import model.database.SlickResourceProvider
import model.database.ixiasmodel.TodoCategoryModel
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

case class IxiasCategoryRepository[P <: JdbcProfile]()(implicit val driver: P)
    extends SlickRepository[TodoCategoryModel.Id, TodoCategoryModel, P]
    with SlickResourceProvider[P] {

  import api._

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
