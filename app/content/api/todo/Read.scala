package content.api.todo

import model.{Todo, TodoCategory}
import play.api.libs.json.{Json, OWrites}
import java.time.format.DateTimeFormatter

case class ReadCategory(id: Long, name: String, slug: String, color: Int)

object ReadCategory {
  implicit val writes: OWrites[ReadCategory] = Json.writes[ReadCategory]

  def build(category: TodoCategory#EmbeddedId): ReadCategory = ReadCategory(
    category.id.longValue(), category.v.name, category.v.slug, category.v.color
  )
}

case class ReadTodo(id: Long, title: String, body: String, date: String)

object ReadTodo {
  implicit val writes: OWrites[ReadTodo] = Json.writes[ReadTodo]

  private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss")

  private def build(todo: Todo#EmbeddedId): ReadTodo = ReadTodo(
    todo.id.longValue(), todo.v.title, todo.v.body, todo.v.updatedAt.format(DATE_FORMATTER)
  )

  def builds(todos: Seq[Todo#EmbeddedId]): Seq[ReadTodo] =
    todos.sortWith((a, b) => a.v.updatedAt.isAfter(b.v.updatedAt)).map(build)
}

case class ReadState(state: Short, todos: Seq[ReadTodo])

object ReadState {
  implicit val writes: OWrites[ReadState] = Json.writes[ReadState]

  def builds(todos: Seq[Todo#EmbeddedId]): Seq[ReadState] = {
    todos.groupBy(_.v.state).map { case (state, todo) =>
      ReadState(state.code, ReadTodo.builds(todo))
    }.toSeq.sortWith(_.state < _.state)
  }
}

case class Read(category: ReadCategory, states: Seq[ReadState])

object Read {
  implicit val writes: OWrites[Read] = Json.writes[Read]

  def build(category: TodoCategory#EmbeddedId, todos: Seq[Todo#EmbeddedId]): Read = Read(
    ReadCategory.build(category), ReadState.builds(todos)
  )
}

