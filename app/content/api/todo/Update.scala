package content.api.todo

import model.{Todo, TodoCategory}
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Json, Reads}
import validation._

/* implicitが必要 */
import content.api.Utility._

case class UpdateTodo(id: Todo.Id, categoryId: TodoCategory.Id, title: String, body: String, state: Todo.State) {
  def value: Todo#EmbeddedId = Todo.embeddedId(
    id,
    categoryId,
    title,
    body,
    state
  )
}

object UpdateTodo {
  implicit val reads: Reads[UpdateTodo] = (
    (JsPath \ "id").read[Todo.Id] and
      (JsPath \ "categoryId").read[TodoCategory.Id] and
      (JsPath \ "title").read[String](api.TodoValidation.title) and
      (JsPath \ "body").read[String](api.TodoValidation.body) and
      (JsPath \ "state").read[Todo.State]
    )(UpdateTodo.apply _)
}

case class Update(todo: UpdateTodo) {
  def value: Todo#EmbeddedId = todo.value
}

object Update {
  implicit val reads: Reads[Update] = Json.reads[Update]
}
