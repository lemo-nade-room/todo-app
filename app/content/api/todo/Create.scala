package content.api.todo

import ixias.util.json.JsonEnvReads
import model.{Todo, TodoCategory}
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.Reads.email
import play.api.libs.json.{JsPath, Json, Reads}
import validation._

/* implicitが必要 */
import content.api.Utility._

case class CreateTodo(categoryId: TodoCategory.Id, title: String, body: String) {
  def value: Todo#WithNoId = Todo.withNoId(
    categoryId, title, body, Todo.State.PENDING
  )
}

private object CreateTodo extends JsonEnvReads {

  implicit val reads: Reads[CreateTodo] = (
    (JsPath \ "categoryId").read[TodoCategory.Id] and
      (JsPath \ "title").read[String](api.TodoValidation.title) and
      (JsPath \ "body").read[String](api.TodoValidation.body)
    )(CreateTodo.apply _)
}


case class Create(todo: CreateTodo) {
  def value: Todo#WithNoId = todo.value
}

object Create {
  implicit val reads: Reads[Create] = Json.reads[Create]
}
