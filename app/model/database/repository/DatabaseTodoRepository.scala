package model.database.repository

import model.content.TodoContent
import model.database.model.TodoModel
import model.repository.TodoRepository
import model.database.{Connection, Table}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.CanBeQueryCondition.BooleanColumnCanBeQueryCondition
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class DatabaseTodoRepository() extends TodoRepository {

  override def create(todo: TodoContent.Create): Future[Long] = Connection.db.run {
    Table.todos returning Table.todos.map(_.id) += TodoModel(0, todo.categoryId, todo.title, todo.body, 0)
  }

  override def update(todo: TodoContent.Update): Future[Unit] = Connection.db.run {
    Table.todos
      .filter(_.id === todo.todoId)
      .map(todo => (todo.title, todo.body, todo.state, todo.categoryId))
      .update((todo.title, todo.body, todo.state, todo.categoryId))
      .map(_ => Unit)
  }

  override def all(): Future[Seq[TodoContent.View]] = {
    for (todos <- Connection.db.run(Table.todos.result)) yield todos.map(_.convertView)
  }

  override def delete(todoId: Long): Future[Unit] = Connection.db.run {
    Table.todos.filter(_.id === todoId).delete
  }.map(_ => Unit)
}
