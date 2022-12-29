package content.api.todo

import model.{Todo, TodoCategory}
import play.api.libs.json.{Json, Reads}

case class UpdateTodo(id: Long, categoryId: Long, title: String, body: String, state: Short) {
  def value: Todo#EmbeddedId = Todo.embeddedId(
    Todo.Id(id.asInstanceOf[Todo.Id.U]),
    TodoCategory.Id(categoryId.asInstanceOf[TodoCategory.Id.U]),
    title,
    body,
    Todo.State.of(state)
  )
}

object UpdateTodo {
  implicit val reads: Reads[UpdateTodo] = Json.reads[UpdateTodo]
}

case class Update(todo: UpdateTodo) {
  def value: Todo#EmbeddedId = todo.value
}

object Update {
  implicit val reads: Reads[Update] = Json.reads[Update]
}
