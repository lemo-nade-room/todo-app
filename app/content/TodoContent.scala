package content

import model.Todo

object TodoContent {
  case class View
  (
    id: Long,
    title: String,
    body: String,
    state: Int,
  )

  object View {
    def make(todo: Todo#EmbeddedId): View = View(
      id = todo.id.longValue(),
      title = todo.v.title,
      body = todo.v.body,
      state = todo.v.state.code,
    )

    def makeViews(todos: Seq[Todo#EmbeddedId]): Seq[View] = todos
      .sortWith((a, b) => a.v.updatedAt.isAfter(b.v.updatedAt))
      .map(View.make)
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
