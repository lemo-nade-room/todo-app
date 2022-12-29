package content.api.todo

import ixias.util.json.JsonEnvReads
import model.{Todo, TodoCategory}
import play.api.libs.json.{Json, Reads}

/* implicitが必要 */
import content.api.Utility._

case class CreateTodo(categoryId: TodoCategory.Id, title: String, body: String) {
  def value: Todo#WithNoId = Todo.withNoId(
    categoryId, title, body, Todo.State.PENDING
  )
}

private object CreateTodo extends JsonEnvReads {
  implicit val reads: Reads[CreateTodo] = Json.reads[CreateTodo]
}

case class Create(todo: CreateTodo) {
  def value: Todo#WithNoId = todo.value
}

object Create {
  implicit val reads: Reads[Create] = Json.reads[Create]
}
