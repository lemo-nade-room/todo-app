package content.api

import model.TodoCategory
import play.api.libs.json.{Format, Json, OWrites, Reads}

/* implicitが必要 */
import content.api.Utility._

object ApiCategoryContent {

  case class WithNoIdContent(name: String, slug: String, color: Int) {
    def value: TodoCategory#WithNoId = TodoCategory.withNoId(name, slug, color)
  }

  object WithNoIdContent {
    implicit val reads: Reads[WithNoIdContent] = Json.reads[WithNoIdContent]
  }

  case class EmbeddedIdContent(id: TodoCategory.Id, name: String, slug: String, color: Int) {
    def value: TodoCategory#EmbeddedId = TodoCategory.embeddedId(id, name, slug, color)
  }

  object EmbeddedIdContent {
    implicit val format: Format[EmbeddedIdContent] = Json.format[EmbeddedIdContent]

    def build(category: TodoCategory#EmbeddedId): EmbeddedIdContent = EmbeddedIdContent(
      category.id, category.v.name, category.v.slug, category.v.color
    )
  }

  case class All(categories: Seq[EmbeddedIdContent])

  object All {
    implicit val writes: OWrites[All] = Json.writes[All]

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