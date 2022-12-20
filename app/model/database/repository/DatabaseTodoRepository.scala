package model.database.repository

import model.database.model.TodoModel
import model.repository.TodoRepository
import model.database.{Connection, Table}
import model.entity.Todo
import model.entity.todo.{TodoBody, TodoCategory, TodoID, TodoState, TodoTitle}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.CanBeQueryCondition.BooleanColumnCanBeQueryCondition
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class DatabaseTodoRepository() extends TodoRepository {


  override def create(title: TodoTitle, body: TodoBody, state: TodoState, category: TodoCategory): Future[Todo] = {
    val newTodoModel = TodoModel.make(title, body, state, category)
    val insertQuery = Table.todos returning Table.todos.map(_.id) += newTodoModel
    for {
      id <- Connection.db.run(insertQuery)
      createdModel <- Connection.db.run(Table.todos.filter(_.id === id).result.head)
    } yield createdModel.todo(category)
  }

  override def update(todo: Todo): Future[Unit] = Connection.db.run {
    Table.todos
      .filter(_.id === todo.id.id)
      .map(model => (model.title, model.body, model.state, model.categoryId))
      .update((todo.title.title, todo.body.body, todo.state.state, todo.category.id.id))
      .map(_ => Unit)
  }

  override def all(): Future[Seq[Todo]] = {
    Connection.db.run {
      Table.todos
        .join(Table.todoCategories)
        .on(_.categoryId === _.id)
        .result
    }.map(_.map(tuple => {
      val (todoModel, categoryModel) = tuple
      todoModel.todo(categoryModel.category)
    }))
  }

  override def delete(id: TodoID): Future[Unit] = Connection.db.run {
    Table.todos.filter(_.id === id.id).delete
  }.map(_ => Unit)
}
