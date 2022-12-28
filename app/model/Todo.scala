package model

import ixias.model._
import ixias.util.EnumStatus
import model.Todo._

import java.time.LocalDateTime

case class Todo
(
  id: Option[Id],
  category: TodoCategory.Id,
  title: String,
  body: String,
  state: State,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
) extends EntityModel[Id] {

  /** require id is not NULL */
  def todo(category: TodoCategory): Todo = {
    Todo(
      new TodoID(id.get),
      category,
      new TodoTitle(title),
      new TodoBody(body),
      TodoState(state.code),
      createdAt,
      updatedAt,
    )
  }

}


object Todo {

  val Id: Identity[Id] = the[Identity[Id]]
  type Id = Long @@ Todo
  type WithNoId = Entity.WithNoId[Id, Todo]
  type EmbeddedId = Entity.EmbeddedId[Id, Todo]

  sealed abstract class State(val code: Short) extends EnumStatus

  object State extends EnumStatus.Of[State] {
    case object PENDING extends State(0)

    case object PROGRESS extends State(1)

    case object DONE extends State(2)

    def of(state: TodoState): State = {
      State.values.filter(_.code.toInt == state.value).head
    }
  }

  def build(categoryId: CategoryID, title: TodoTitle, body: TodoBody, state: TodoState): WithNoId = Entity.WithNoId(
    Todo(None, TodoCategory.id(categoryId), title.value, body.value, State.of(state))
  )

  def build(id: TodoID, categoryId: CategoryID, title: TodoTitle, body: TodoBody, state: TodoState): EmbeddedId = Entity.EmbeddedId(
    Todo(Some(this.id(id)), TodoCategory.Id(categoryId.value.asInstanceOf[TodoCategory.Id.U]), title.value, body.value, State.of(state))
  )

  def id(id: TodoID): Id = Id(id.value.asInstanceOf[Id.U])
}
