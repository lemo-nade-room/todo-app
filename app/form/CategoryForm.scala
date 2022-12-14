package form

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, number, text}
import validation.CategoryValidation
import content.CategoryContent._

object CategoryForm {
  lazy val createForm: Form[Create] = Form(mapping(
    "name" -> text.verifying(CategoryValidation.nameConstraint),
    "slug" -> text.verifying(CategoryValidation.slugConstraint),
    "color" -> number,
  )(Create.apply)(Create.unapply))

  lazy val updateForm: Form[Update] = Form(mapping(
    "id" -> longNumber,
    "name" -> text.verifying(CategoryValidation.nameConstraint),
    "slug" -> text.verifying(CategoryValidation.slugConstraint),
    "color" -> number,
  )(Update.apply)(Update.unapply))

  lazy val deleteForm: Form[Delete] = Form(mapping(
    "id" -> longNumber,
  )(Delete.apply)(Delete.unapply))
}
