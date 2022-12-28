package content

import model.{Todo, TodoCategory}

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
    def makeViews(todoAndCategories: Seq[(Option[Todo#EmbeddedId], TodoCategory#EmbeddedId)]): Seq[View] = {
      todoAndCategories.groupBy(_._2)
        .map { case (category, categoryTodos) =>
          View.make(category, categoryTodos.map(_._1).collect { case Some(v) => v })
        }.toSeq.sortWith(_.name < _.name)
    }

    def make(category: TodoCategory#EmbeddedId, todos: Seq[Todo#EmbeddedId]): View = View(
      id = category.id.longValue(),
      name = category.v.name,
      slug = category.v.slug,
      color = category.v.color,
      todos = TodoContent.View.makeViews(todos)
    )
  }

  case class Create(name: String, slug: String, color: Int) {
    def category: TodoCategory#WithNoId = TodoCategory.withNoId(name, slug, color)
  }

  case class Update(id: Long, name: String, slug: String, color: Int) {
    def category: TodoCategory#EmbeddedId = TodoCategory.embeddedId(
      TodoCategory.Id(id.asInstanceOf[TodoCategory.Id.U]), name, slug, color
    )
  }

  case class Delete(id: Long) {
    def categoryId: TodoCategory.Id = TodoCategory.Id(id.asInstanceOf[TodoCategory.Id.U])
  }
}
