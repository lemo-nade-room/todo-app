package model.content

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, number, text}


object TodoContent {
  case class View
  (
    id: Long,
    title: String,
    body: String,
    state: TodoStateContent,
  )

  case class Create(title: String, body: String, categoryId: Long)

  lazy val createForm: Form[Create] = Form(mapping(
    "title" -> text,
    "body" -> text,
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
    "title" -> text,
    "body" -> text,
    "state" -> number,
    "category" -> longNumber,
  )(Update.apply)(Update.unapply))

}
