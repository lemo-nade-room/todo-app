package model.content

import model.entity.Todo

object TodoContent {
  case class View
  (
    id: Long,
    title: String,
    body: String,
    state: Int,
  )

  object View {
    def make(todo: Todo): View = View (
      todo.id.id,
      todo.title.title,
      todo.body.body,
      todo.state.state,
    )
  }

  case class Create(title: String, body: String, categoryId: Long)

  case class Update
  (
    todoId: Long,
    title: String,
    body: String,
    state: Int,
    categoryId: Long,
  )

  case class Delete(id: Long)
}
