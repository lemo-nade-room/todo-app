package content.api.todo

import model.{Todo, TodoCategory}
import play.api.libs.json.{Json, Writes}

import java.time.format.DateTimeFormatter

/* implicitが必要 */
import content.api.Utility._

case class ReadCategory(id: TodoCategory.Id, name: String, slug: String, color: Int)

object ReadCategory {
  implicit val writes: Writes[ReadCategory] = Json.writes[ReadCategory]

  def build(category: TodoCategory#EmbeddedId): ReadCategory = ReadCategory(
    category.id, category.v.name, category.v.slug, category.v.color
  )
}

case class ReadTodo(id: Todo.Id, title: String, body: String, date: String)

object ReadTodo {
  implicit val writes: Writes[ReadTodo] = Json.writes[ReadTodo]

  private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

  private def build(todo: Todo#EmbeddedId): ReadTodo = ReadTodo(
    todo.id, todo.v.title, todo.v.body, todo.v.updatedAt.format(DATE_FORMATTER)
  )

  def builds(todos: Seq[Todo#EmbeddedId]): Seq[ReadTodo] =
    todos.sortWith((a, b) => a.v.updatedAt.isAfter(b.v.updatedAt)).map(build)
}

case class ReadState(state: Short, todos: Seq[ReadTodo])

object ReadState {
  implicit val writes: Writes[ReadState] = Json.writes[ReadState]

  def builds(todos: Seq[Todo#EmbeddedId]): Seq[ReadState] = {
    todos.groupBy(_.v.state).map { case (state, todo) =>
      ReadState(state.code, ReadTodo.builds(todo))
    }.toSeq.sortWith(_.state < _.state)
  }
}

case class Read(category: ReadCategory, states: Seq[ReadState])

object Read {
  implicit val writes: Writes[Read] = Json.writes[Read]

  def build(category: TodoCategory#EmbeddedId, todos: Seq[Todo#EmbeddedId]): Read = Read(
    ReadCategory.build(category), ReadState.builds(todos)
  )
}

