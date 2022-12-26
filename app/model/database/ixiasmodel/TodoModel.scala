package model.database.ixiasmodel

import ixias.model._
import ixias.util.EnumStatus
import model.database.ixiasmodel.TodoModel._
import model.entity.Todo
import model.entity.todo.category.{CategoryID, CategoryName}
import model.entity.todo.{TodoBody, TodoCategory, TodoID, TodoState, TodoTitle}

import java.time.LocalDateTime

case class TodoModel
(
  id: Option[Id],
  category: TodoCategoryModel.Id,
  title: String,
  body: String,
  state: State,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
) extends EntityModel[Id] {

  /** require id is not NULL */
  def todo(category: TodoCategory): Todo = Todo(
    TodoID(id.get),
    category,
    TodoTitle(title),
    TodoBody(body),
    TodoState(state.code),
    createdAt,
    updatedAt,
  )
}


object TodoModel {

  val Id: Identity[Id] = the[Identity[Id]]
  type Id = Long @@ TodoModel
  type WithNoId = Entity.WithNoId[Id, TodoModel]
  type EmbeddedId = Entity.EmbeddedId[Id, TodoModel]

  sealed abstract class State(val code: Short) extends EnumStatus

  object State extends EnumStatus.Of[State] {
    case object PENDING extends State(0)

    case object PROGRESS extends State(1)

    case object DONE extends State(2)

    def of(state: TodoState): State = {
      State.values.filter(_.code == state.value).head
    }
  }

  def build(categoryId: CategoryID, title: TodoTitle, body: TodoBody, state: TodoState): WithNoId = Entity.WithNoId(
    TodoModel(None, TodoCategoryModel.Id(categoryId.value), title.value, body.value, State.of(state))
  )
}
