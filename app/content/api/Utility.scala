package content.api

import ixias.util.json.{JsonEnvReads, JsonEnvWrites}
import model.{Todo, TodoCategory}
import play.api.libs.json.{Format, Writes}

object Utility extends JsonEnvReads with JsonEnvWrites {

  implicit val categoryId: Format[TodoCategory.Id] = Format(
    idAsNumberReads[TodoCategory.Id],
    Writes.of[Long].contramap(_.longValue())
  )

  implicit val todoId: Format[Todo.Id] = Format(
    idAsNumberReads[Todo.Id],
    Writes.of[Long].contramap(_.longValue())
  )


  implicit val todoState: Format[Todo.State] = Format(
    enumReads(Todo.State),
    EnumStatusWrites.contramap(identity)
  )
}