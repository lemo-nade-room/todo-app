package model.database.repository

import com.google.inject.Inject
import ixias.persistence.SlickRepository
import model.database.ixiasmodel.{TodoCategoryModel, TodoModel}
import model.database.table.TodoTable
import model.repository.{CategoryRepository, TodoRepository}
import model.database.{Connection, SlickResourceProvider, ixiasmodel}
import model.entity.Todo
import model.entity.todo.{TodoBody, TodoCategory, TodoID, TodoState, TodoTitle}
import slick.jdbc.{JdbcProfile, MySQLProfile}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.CanBeQueryCondition.BooleanColumnCanBeQueryCondition

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class DatabaseTodoRepository()(implicit val driver: MySQLProfile)
  extends TodoRepository
    with SlickRepository[ixiasmodel.TodoModel.Id, TodoModel, MySQLProfile]
    with SlickResourceProvider[MySQLProfile] {

  import api._
  override def create(title: TodoTitle, body: TodoBody, state: TodoState, category: TodoCategory): Future[Todo] = {
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

  override def update
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

  override def all(): Future[Seq[Todo]] = {
    DBAction(TodoTable, "slave") { case (db, todo ) =>
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

  override def delete(id: TodoID): Future[Unit] = {
    RunDBAction(TodoTable, "master") { t =>
      t.filter(_.id === TodoModel.Id(id.value.asInstanceOf[TodoModel.Id.U])).delete
    }.map(_ => Unit)
  }

  override def find(id: TodoID): Future[Option[Todo]] = {
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
