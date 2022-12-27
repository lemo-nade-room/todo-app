package model.database.ixiasmodel

import ixias.model._
import model.database.ixiasmodel.TodoCategoryModel._
import model.entity.todo.TodoCategory
import model.entity.todo.category.{CategoryColor, CategoryID, CategoryName, CategorySlug}
import java.time.LocalDateTime

case class TodoCategoryModel
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
    CategoryID(id.get),
    CategoryName(name),
    CategorySlug(slug),
    CategoryColor(color),
    createdAt,
    updatedAt,
  )
}

object TodoCategoryModel {

  val Id: Identity[Id] = the[Identity[Id]]
  type Id = Long @@ TodoCategoryModel
  type WithNoId = Entity.WithNoId[Id, TodoCategoryModel]
  type EmbeddedId = Entity.EmbeddedId[Id, TodoCategoryModel]

  def build(name: String, slug: String, color: Int): WithNoId = Entity.WithNoId(
    TodoCategoryModel(None, name, slug, color)
  )

  def build(id: CategoryID, name: CategoryName, slug: CategorySlug, color: CategoryColor): EmbeddedId = Entity.EmbeddedId(
    TodoCategoryModel(Some(Id(id.value.asInstanceOf[Id.U])), name.value, slug.value, color.value)
  )

  def id(categoryID: CategoryID): Id = Id(categoryID.value.asInstanceOf[Id.U])
}