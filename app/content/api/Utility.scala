package content.api

import ixias.util.json.{JsonEnvReads, JsonEnvWrites}
import model.{Todo, TodoCategory}
import play.api.libs.json.Reads

object Utility extends JsonEnvReads {
  implicit val categoryId: Reads[TodoCategory.Id] = idAsNumberReads[TodoCategory.Id]
  implicit val todoId: Reads[Todo.Id] = idAsNumberReads[Todo.Id]
}