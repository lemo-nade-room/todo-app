package content.api.todo

import model.{Todo, TodoCategory}
import play.api.libs.json.{Json, Writes}

import java.time.format.DateTimeFormatter

/* implicitが必要 */
import content.api.Utility._

case class ReadCategory(id: TodoCategory.Id, name: String, slug: String, color: Int)

case class ReadTodo(id: Todo.Id, title: String, body: String, date: String)

object ReadTodo {
  implicit val writes: Writes[ReadTodo] = Json.writes[ReadTodo]

  private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

  def build(todo: Todo#EmbeddedId): ReadTodo = ReadTodo(
    todo.id, todo.v.title, todo.v.body, todo.v.updatedAt.format(DATE_FORMATTER)
  )
}

case class Read(todos: Seq[ReadTodo])

object Read {
  implicit val writes: Writes[Read] = Json.writes[Read]

  def build(todos: Seq[Todo#EmbeddedId]): Read = Read(
    todos.map(ReadTodo.build)
  )
}

