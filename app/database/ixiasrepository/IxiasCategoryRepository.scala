package database.ixiasrepository

import com.google.inject.Inject
import database.SlickResourceProvider
import ixias.persistence.SlickRepository
import model.{Todo, TodoCategory}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

case class IxiasCategoryRepository[P <: JdbcProfile] @Inject()(implicit val driver: P)
  extends SlickRepository[TodoCategory.Id, TodoCategory, P]
    with SlickResourceProvider[P] {

  import api._

  def allWithTodos(): Future[Seq[(Todo#EmbeddedId, TodoCategory#EmbeddedId)]] = {
    DBAction(TodoCategoryTable, "slave") { case (db, category) =>
      DBAction(TodoTable, "slave") { case (_, todo) =>
        db.run {
          category
            .join(todo)
            .on(_.id === _.categoryId)
            .result
        }.map(_.map(result => (result._2, result._1)))
      }
    }
  }

  def delete(id: Id): Future[Unit] = {
    DBAction(TodoCategoryTable, "master") { case (db, category) =>
      DBAction(TodoTable, "master") { case (_, todo) =>
        val todosDeleteQuery = todo.filter(_.categoryId === id).delete
        val categoryDeleteQuery = category.filter(_.id === id).delete
        db.run((todosDeleteQuery andThen categoryDeleteQuery).transactionally)
      }
    }.map(_ => Unit)
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
    RunDBAction(TodoCategoryTable) { slick =>
      val row = slick.filter(_.id === entity.id)
      for {
        old <- row.result.headOption
        _ <- old match {
          case None => DBIO.successful(0)
          case Some(_) => row.update(entity.v)
        }
      } yield old
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
