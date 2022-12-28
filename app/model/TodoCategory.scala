package model

import ixias.model._
import model.TodoCategory._

import java.time.LocalDateTime

case class TodoCategory
(
  id: Option[Id],
  name: String,
  slug: String,
  color: Int,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
) extends EntityModel[Id] {

  /** require id is not NULL */
  def category: TodoCategory = TodoCategory(
    new CategoryID(id.get),
    new CategoryName(name),
    new CategorySlug(slug),
    new CategoryColor(color),
    createdAt,
    updatedAt,
  )
}

object TodoCategory {

  val Id: Identity[Id] = the[Identity[Id]]
  type Id = Long @@ TodoCategory
  type WithNoId = Entity.WithNoId[Id, TodoCategory]
  type EmbeddedId = Entity.EmbeddedId[Id, TodoCategory]

  def build(name: CategoryName, slug: CategorySlug, color: CategoryColor): WithNoId = Entity.WithNoId(
    TodoCategory(None, name.value, slug.value, color.value)
  )

  def build(id: CategoryID, name: CategoryName, slug: CategorySlug, color: CategoryColor): EmbeddedId = Entity.EmbeddedId(
    TodoCategory(Some(this.id(id)), name.value, slug.value, color.value)
  )

  def id(categoryID: CategoryID): Id = Id(categoryID.value.asInstanceOf[Id.U])
}