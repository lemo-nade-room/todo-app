package database.ixiasrepository

import database.SlickResourceProvider
import scala.concurrent.Future
import slick.jdbc.JdbcProfile
import ixias.persistence.SlickRepository
import model.{Todo, TodoCategory}

case class IxiasTodoRepository[P <: JdbcProfile]()(implicit val driver: P)
  extends SlickRepository[Todo.Id, Todo, P]
    with SlickResourceProvider[P] {

  import api._

  def all(): Future[Seq[EntityEmbeddedId]] = RunDBAction(TodoTable, "slave") { t =>
    t.result
  }

  def all(categoryId: TodoCategory.Id): Future[Seq[EntityEmbeddedId]] = RunDBAction(TodoTable, "slave") { t =>
    t.filter(_.categoryId === categoryId).result
  }

  def get(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") { t =>
      t.filter(_.id === id)
        .result.headOption
    }

  def add(entity: EntityWithNoId): Future[Id] =
    RunDBAction(TodoTable) { slick =>
      slick returning slick.map(_.id) += entity.v
    }.map(_.asInstanceOf[Id])

  def update(entity: EntityEmbeddedId): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.id === entity.id)
      for {
        old <- row.result.headOption
        _ <- old match {
          case None => DBIO.successful(0)
          case Some(_) => row.update(entity.v)
        }
      } yield old
    }

  def remove(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
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