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

  override def create(categoryId: Long, title: String, body: String): Future[Long] = Connection.db.run {
    Table.todos += TodoModel(1L, categoryId, title, body, 0, Timestamp, Timestamp)
  }.map(_.toLong)

  override def update(todoId: Long, title: Option[String], body: Option[String], state: Option[TodoStateContent], categoryId: Option[Long]): Future[Unit] = {
    for (old <- Connection.db.run(Table.todos.filter(_.id == todoId).result.head)) yield {
      val deleteAction = Table.todos.filter(_.id == todoId).delete
      val createAction = Table.todos += TodoModel(
        todoId,
        categoryId.getOrElse(old.categoryId),
        title.getOrElse(old.title),
        body.getOrElse(old.body),
        state.getOrElse(TodoStateContent(old.state)).state,
        Timestamp,
        Timestamp
      )
      for (_ <- Connection.db.run((deleteAction andThen createAction).transactionally)) yield Unit
    }
  }

  override def all(): Future[Seq[TodoContent]] = {
    for (todos <- Connection.db.run(Table.todos.result)) yield todos.map(_.convert)
  }
}