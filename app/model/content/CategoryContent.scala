package model.content

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, number, text}
import validation.CategoryValidation

object CategoryContent {
  case class View
  (
    id: Long,
    name: String,
    slug: String,
    color: Int,
    todos: Seq[TodoContent.View],
  )

  case class Create(name: String, slug: String, color: Int)

  lazy val createForm: Form[Create] = Form(mapping(
    "name" -> text.verifying(CategoryValidation.nameConstraint),
    "slug" -> text.verifying(CategoryValidation.slugConstraint),
    "color" -> number,
  )(Create.apply)(Create.unapply))

  case class Update(id: Long, name: String, slug: String, color: Int)

  lazy val updateForm: Form[Update] = Form(mapping(
    "id" -> longNumber,
    "name" -> text.verifying(CategoryValidation.nameConstraint),
    "slug" -> text.verifying(CategoryValidation.slugConstraint),
    "color" -> number,
  )(Update.apply)(Update.unapply))

}
