package content.api.todo

import model.{Todo, TodoCategory}
import play.api.libs.json.{Json, Reads}

/* implicitが必要 */
import content.api.Utility._

case class UpdateTodo(id: Todo.Id, categoryId: TodoCategory.Id, title: String, body: String, state: Short) {
  def value: Todo#EmbeddedId = Todo.embeddedId(
    id,
    categoryId,
    title,
    body,
    Todo.State.of(state),
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
