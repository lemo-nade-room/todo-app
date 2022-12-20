package model.database.model

import model.entity.Todo
import model.entity.todo.{TodoBody, TodoCategory, TodoID, TodoState, TodoTitle}

import java.sql.Timestamp
import java.time.LocalDateTime

case class TodoModel
(
  id: Long = 0,
  categoryId: Long,
  title: String,
  body: String,
  state: Int,
  updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
  createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
) {
  def todo(category: TodoCategory): Todo = Todo(
    TodoID(id),
    category,
    TodoTitle(title),
    TodoBody(body),
    TodoState(state),
    createdAt.toLocalDateTime,
    updatedAt.toLocalDateTime,
  )
}

object TodoModel {
  def make(title: TodoTitle, body: TodoBody, state: TodoState, category: TodoCategory): TodoModel = TodoModel(
    0, category.id.id, title.title, body.body, state.state
  )
}