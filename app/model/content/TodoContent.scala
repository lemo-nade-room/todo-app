package model.content

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, number, text}
import validation.TodoValidation


object TodoContent {
  case class View
  (
    id: Long,
    title: String,
    body: String,
    state: Int,
  )

  case class Create(title: String, body: String, categoryId: Long)

  lazy val createForm: Form[Create] = Form(mapping(
    "title" -> text.verifying(TodoValidation.titleConstraint),
    "body" -> text.verifying(TodoValidation.bodyConstraint),
    "categoryId" -> longNumber,
  )(Create.apply)(Create.unapply))

  case class Update
  (
    todoId: Long,
    title: String,
    body: String,
    state: Int,
    categoryId: Long,
  )

  lazy val updateForm: Form[Update] = Form(mapping(
    "todoId" -> longNumber,
    "title" -> text.verifying(TodoValidation.titleConstraint),
    "body" -> text.verifying(TodoValidation.bodyConstraint),
    "state" -> number(min = 0, max = 2),
    "category" -> longNumber,
  )(Update.apply)(Update.unapply))

}
