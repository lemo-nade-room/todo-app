package model.content

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

  case class Update(id: Long, name: String, slug: String, color: Int)

  case class Delete(id: Long)
}
