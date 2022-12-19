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

  case class Update
  (
    todoId: Long,
    title: String,
    body: String,
    state: Int,
    categoryId: Long,
  )

  case class Delete(id: Long)
}
