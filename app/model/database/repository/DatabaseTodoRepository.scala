package model.database.repository

import model.content.{TodoContent, TodoStateContent}
import model.database.model.TodoModel
import model.repository.TodoRepository
import model.database.{Connection, Table}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.CanBeQueryCondition.BooleanColumnCanBeQueryCondition

import java.sql.Timestamp
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DatabaseTodoRepository extends TodoRepository {

  override def create(categoryId: Long, title: String, body: String): Future[TodoContent] = {
    Future {
      val newTodo = TodoModel(1, categoryId, title, body, 0, Timestamp, Timestamp)
      val todoIdFuture = Connection.db.run(Table.todos += newTodo)
      val todoId = Connection.await(todoIdFuture)
      val todoModel = Connection.await(findTodoModel(todoId)).get
      convert(todoModel)
    }
  }

  private def findTodoModel(todoId: Long): Future[Option[TodoModel]] = {
    Future {
      val query = Table.todos.filter(_.id == todoId).result
      val resultFuture = Connection.db.run(query)
      val todos = Connection.await(resultFuture)
      todos.headOption match {
        case Some(todoModel) => Option(todoModel)
        case None => Option.empty
      }
    }
  }

  override def update(todoId: Long, title: Option[String], body: Option[String], state: Option[TodoStateContent], categoryId: Option[Long]): Future[Unit] = {
    Future {
      val old = Connection.await(findTodoModel(todoId)) match {
        case Some(value) => value
        case None => throw IllegalArgumentException
      }

      val deleteQuery = Table.todos.filter(_.id == todoId).delete

      val newTodoModel = TodoModel(
        todoId,
        categoryId.getOrElse(old.categoryId),
        title.getOrElse(old.title),
        body.getOrElse(old.body),
        state.getOrElse(TodoStateContent(old.state)).state,
        Timestamp,
        Timestamp
      )
      val insertQuery = Table.todos += newTodoModel

      val transaction = deleteQuery andThen insertQuery
      Connection.await(Connection.db.run(transaction.transactionally))
    }
  }

  override def all(): Future[List[TodoContent]] = {
    Future {
      val allTodosFuture = Connection.db.run(Table.todos.result)
      val allTodoModels = Connection.await(allTodosFuture)
      allTodoModels.map(convert(_)).toList
    }
  }

  private def convert(model: TodoModel): TodoContent = {
    TodoContent(model.id, model.title, model.body, TodoStateContent(model.state))
  }
}
