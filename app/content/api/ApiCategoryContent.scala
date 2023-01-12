package content.api

import model.TodoCategory
import play.api.libs.functional.syntax.{toApplicativeOps, toFunctionalBuilderOps}
import play.api.libs.json.{Format, JsPath, Json, Reads, Writes}
import validation._

/* implicitが必要 */
import content.api.Utility._

object ApiCategoryContent {

  case class WithNoIdContent(name: String, slug: String, color: Int) {
    def value: TodoCategory#WithNoId = TodoCategory.withNoId(name, slug, color)
  }

  object WithNoIdContent {
    implicit val reads: Reads[WithNoIdContent] = (
        (JsPath \ "name").read[String](api.CategoryValidation.name) and
        (JsPath \ "slug").read[String](api.CategoryValidation.slug) and
        (JsPath \ "color").read[Int](Reads.min(1).keepAnd(Reads.max(3)))
      )(WithNoIdContent.apply _)
  }

  case class EmbeddedIdContent(id: TodoCategory.Id, name: String, slug: String, color: Int) {
    def value: TodoCategory#EmbeddedId = TodoCategory.embeddedId(id, name, slug, color)
  }

  object EmbeddedIdContent {
    private val writes = Json.writes[EmbeddedIdContent]
    private val reads: Reads[EmbeddedIdContent] = (
      (JsPath \ "id").read[TodoCategory.Id] and
        (JsPath \ "name").read[String](api.CategoryValidation.name) and
        (JsPath \ "slug").read[String](api.CategoryValidation.slug) and
        (JsPath \ "color").read[Int](Reads.min(1).keepAnd(Reads.max(3)))
      )(EmbeddedIdContent.apply _)
    implicit val format: Format[EmbeddedIdContent] = Format(reads, writes)

    def build(category: TodoCategory#EmbeddedId): EmbeddedIdContent = EmbeddedIdContent(
      category.id, category.v.name, category.v.slug, category.v.color
    )
  }

  case class All(categories: Seq[EmbeddedIdContent])

  object All {
    implicit val writes: Writes[All] = Json.writes[All]

    def build(categories: Seq[TodoCategory#EmbeddedId]): All = All(
      categories.map(EmbeddedIdContent.build)
    )
  }

  case class Create(category: WithNoIdContent) {
    def value: TodoCategory#WithNoId = category.value
  }

  object Create {
    implicit val reads: Reads[Create] = Json.reads[Create]
  }

  case class Update(category: EmbeddedIdContent) {
    def value: TodoCategory#EmbeddedId = category.value
  }

  object Update {
    implicit val reads: Reads[Update] = Json.reads[Update]
  }

}