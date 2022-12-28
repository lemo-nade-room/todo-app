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
) extends EntityModel[Id]

object TodoCategory {

  val Id = the[Identity[Id]]
  type Id = Long @@ TodoCategory
  type WithNoId = Entity.WithNoId[Id, TodoCategory]
  type EmbeddedId = Entity.EmbeddedId[Id, TodoCategory]

  def withNoId(name: String, slug: String, color: Int): WithNoId = Entity.WithNoId(
    TodoCategory(None, name, slug, color)
  )

  def embeddedId(id: Id, name: String, slug: String, color: Int): EmbeddedId = Entity.EmbeddedId(
    TodoCategory(Some(id), name, slug, color)
  )
}