package form

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, number, text}
import validation.TodoValidation

object TodoForm {
  lazy val createForm: Form[Create] = Form(mapping(
    "title" -> text.verifying(TodoValidation.titleConstraint),
    "body" -> text.verifying(TodoValidation.bodyConstraint),
    "categoryId" -> longNumber,
  )(Create.apply)(Create.unapply))

  lazy val updateForm: Form[Update] = Form(mapping(
    "todoId" -> longNumber,
    "title" -> text.verifying(TodoValidation.titleConstraint),
    "body" -> text.verifying(TodoValidation.bodyConstraint),
    "state" -> number(min = 0, max = 2),
    "category" -> longNumber,
  )(Update.apply)(Update.unapply))

  lazy val deleteForm: Form[Delete] = Form(mapping(
    "id" -> longNumber,
  )(Delete.apply)(Delete.unapply))
}
