package model.content

import model.entity.Todo
import model.entity.todo.TodoCategory
import model.entity.todo.category.{CategoryID, CategoryName}

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
      category.id.id,
      category.name.name,
      category.slug.slug,
      category.color.color,
      todos.map(TodoContent.View.make)
    )
  }

  case class Create(name: String, slug: String, color: Int)

  case class Update(id: Long, name: String, slug: String, color: Int)

  case class Delete(id: Long)
}
