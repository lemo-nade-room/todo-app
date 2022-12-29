package content.api.todo

import model.{Todo, TodoCategory}
import play.api.libs.json.{Json, Reads}

case class CreateTodo(categoryId: Long, title: String, body: String) {
  def value: Todo#WithNoId = Todo.withNoId(
    TodoCategory.Id(categoryId.asInstanceOf[TodoCategory.Id.U]), title, body, Todo.State.PENDING
  )
}

object CreateTodo {
  implicit val reads: Reads[CreateTodo] = Json.reads[CreateTodo]
}

case class Create(todo: CreateTodo) {
  def value: Todo#WithNoId = todo.value
}

object Create {
  implicit val reads: Reads[Create] = Json.reads[Create]
}
