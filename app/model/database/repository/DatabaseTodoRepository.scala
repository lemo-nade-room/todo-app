package model.database.repository

import model.content.{TodoContent, TodoStateContent}
import model.database.model.TodoModel
import model.repository.TodoRepository
import model.database.{Connection, Table}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.CanBeQueryCondition.BooleanColumnCanBeQueryCondition
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class DatabaseTodoRepository() extends TodoRepository {

  override def create(categoryId: Long, title: String, body: String): Future[Long] = Connection.db.run {
    Table.todos returning Table.todos.map(_.id) += TodoModel(0, categoryId, title, body, 0)
  }

  override def update
  (
    todoId: Long,
    title: String,
    body: String,
    state: TodoStateContent,
    categoryId: Long
  ): Future[Unit] = Connection.db.run {
    Table.todos
      .filter(_.id === todoId)
      .map(todo => (todo.title, todo.body, todo.state, todo.categoryId))
      .update((title, body, state.state, categoryId))
      .map(_ => Unit)
  }

  override def all(): Future[Seq[TodoContent]] = {
    for (todos <- Connection.db.run(Table.todos.result)) yield todos.map(_.convert)
  }

  override def delete(todoId: Long): Future[Unit] = Connection.db.run {
    Table.todos.filter(_.id === todoId).delete
  }.map(_ => Unit)
}
