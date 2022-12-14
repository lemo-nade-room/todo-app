package model.content

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, number, text}

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
    "name" -> text,
    "slug" -> text,
    "color" -> number,
  )(Create.apply)(Create.unapply))

  case class Update(id: Long, name: String, slug: String, color: Int)

  lazy val updateForm: Form[Update] = Form(mapping(
    "id" -> longNumber,
    "name" -> text,
    "slug" -> text,
    "color" -> number,
  )(Update.apply)(Update.unapply))

}
