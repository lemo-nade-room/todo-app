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
      todo.id.value,
      todo.title.value,
      todo.body.value,
      todo.state.value,
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
