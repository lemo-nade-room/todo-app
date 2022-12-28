package content

import model.Todo.Id
import model.{Todo, TodoCategory}

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

  case class Create(title: String, body: String, categoryId: Long) {
    def todo: Todo#WithNoId = Todo.withNoId(
      TodoCategory.Id(categoryId.asInstanceOf[TodoCategory.Id.U]),
      title,
      body,
      Todo.State.PENDING
    )
  }

  case class Update
  (
    todoId: Long,
    title: String,
    body: String,
    state: Int,
    categoryId: Long,
  ) {
    def todo: Todo#EmbeddedId = Todo.embeddedId(
      Todo.Id(todoId.asInstanceOf[Todo.Id.U]),
      TodoCategory.Id(categoryId.asInstanceOf[TodoCategory.Id.U]),
      title,
      body,
      Todo.State.of(state)
    )
  }

  case class Delete(id: Long) {
    def todoId: Id = Todo.Id(id.asInstanceOf[Todo.Id.U])
  }
}
