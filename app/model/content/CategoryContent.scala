package model.content

case class CategoryContent
(
  id: Long,
  name: String,
  slug: String,
  color: Int,
  todos: Seq[TodoContent],
)
