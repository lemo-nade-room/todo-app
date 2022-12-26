package model.database.ixiasrepository

import scala.concurrent.Future
import slick.jdbc.JdbcProfile
import ixias.persistence.SlickRepository
import model.database.SlickResourceProvider
import model.database.ixiasmodel.{TodoCategoryModel, TodoModel}
import model.entity.Todo
import model.entity.todo.{TodoBody, TodoCategory, TodoID, TodoState, TodoTitle}

case class IxiasTodoRepository[P <: JdbcProfile]()(implicit val driver: P)
  extends SlickRepository[TodoModel.Id, TodoModel, P]
    with SlickResourceProvider[P] {

  import api._

  def create(title: TodoTitle, body: TodoBody, state: TodoState, category: TodoCategory): Future[Todo] = {
    val newTodoModel = TodoModel(None, TodoCategoryModel.Id(category.id.value), title.value, body.value, TodoModel.State.of(state))
    for {
      todoId <- RunDBAction(TodoTable, "master") { t =>
        t returning t.map(_.id) += newTodoModel
      }
      todoModel <- RunDBAction(TodoTable, "slave") { t =>
        t.filter(_.id === todoId).result.head
      }
    } yield todoModel.todo(category)
  }

  def update
  (
    id: TodoID,
    title: TodoTitle,
    body: TodoBody,
    state: TodoState,
    category: TodoCategory
  ): Future[Unit] = RunDBAction(TodoTable, "master") { t =>
    t.filter(_.id === TodoModel.Id(id.value.asInstanceOf[TodoModel.Id.U]))
      .map(model => (model.title, model.body, model.state, model.categoryId))
      .update((title.value, body.value, TodoModel.State.of(state), TodoCategoryModel.Id(category.id.value.asInstanceOf[TodoCategoryModel.Id.U])))
  }.map(_ => Unit)

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

  def delete(id: TodoID): Future[Unit] = {
    RunDBAction(TodoTable, "master") { t =>
      t.filter(_.id === TodoModel.Id(id.value.asInstanceOf[TodoModel.Id.U])).delete
    }.map(_ => Unit)
  }

  def find(id: TodoID): Future[Option[Todo]] = {
    DBAction(TodoTable, "slave") { case (db, todo) =>
      DBAction(TodoCategoryTable, "slave") { case (_, category) =>
        db.run {
          todo
            .join(category)
            .on(_.categoryId === _.id)
            .filter(_._1.id === TodoModel.Id(id.value.asInstanceOf[TodoModel.Id.U]))
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
    RunDBAction(TodoTable, "slave") { _
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
        _   <- old match {
          case None    => DBIO.successful(0)
          case Some(_) => row.update(entity.v)
        }
      } yield old
    }

  def remove(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.id === id)
      for {
        old <- row.result.headOption
        _   <- old match {
          case None    => DBIO.successful(0)
          case Some(_) => row.delete
        }
      } yield old
    }
}