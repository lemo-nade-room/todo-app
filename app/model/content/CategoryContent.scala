package model.content

import model.entity.Todo
import model.entity.todo.TodoCategory

object CategoryContent {
  case class View
  (
    id: Long,
    name: String,
    slug: String,
    color: Int,
    todos: Seq[TodoContent.View],
  )

  object View {
    def make(category: TodoCategory, todos: Seq[Todo]): View = View (
      category.id.value,
      category.name.value,
      category.slug.value,
      category.color.value,
      todos.map(TodoContent.View.make)
    )
  }

  case class Create(name: String, slug: String, color: Int)

  case class Update(id: Long, name: String, slug: String, color: Int)

  case class Delete(id: Long)
}
