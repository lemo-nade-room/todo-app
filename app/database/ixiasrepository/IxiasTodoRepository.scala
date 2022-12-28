package database.ixiasrepository

import database.SlickResourceProvider

import scala.concurrent.Future
import slick.jdbc.JdbcProfile
import ixias.persistence.SlickRepository
import model.Todo
import model.entity.Todo
import model.entity.todo.{TodoBody, TodoCategory, TodoID, TodoState, TodoTitle}

case class IxiasTodoRepository[P <: JdbcProfile]()(implicit val driver: P)
  extends SlickRepository[Todo.Id, Todo, P]
    with SlickResourceProvider[P] {

  import api._

  def create(title: TodoTitle, body: TodoBody, state: TodoState, category: TodoCategory): Future[TodoID] = {
    val newTodoModel = Todo.build(category.id, title, body, state)
    for (id <- add(newTodoModel)) yield new TodoID(id.longValue())
  }

  def all(): Future[Seq[Todo]] = {
    DBAction(TodoTable, "slave") { case (db, todo) =>
      DBAction(TodoCategoryTable, "slave") { case (_, category) =>
        db.run {
          todo
            .join(category)
            .on(_.categoryId === _.id)
            .result
        }.map(_.map(tuple => {
          val (todoModel, categoryModel) = tuple
          todoModel.todo(categoryModel.category)
        }))
      }
    }
  }

  def find(id: TodoID): Future[Option[Todo]] = {
    DBAction(TodoTable, "slave") { case (db, todo) =>
      DBAction(TodoCategoryTable, "slave") { case (_, category) =>
        db.run {
          todo
            .join(category)
            .on(_.categoryId === _.id)
            .filter(_._1.id === Todo.id(id))
            .result
            .headOption
        }.map(_.map(tuple => {
          val (todoModel, categoryModel) = tuple
          todoModel.todo(categoryModel.category)
        }))
      }
    }
  }

  def get(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") {
      _
        .filter(_.id === id)
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