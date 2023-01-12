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
) extends EntityModel[Id]

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

    def of(value: Int): State = values.find(_.code.toInt == value) match {
      case Some(v) => v
      case None => throw new IllegalArgumentException(s"Stateが${value}で初期化された")
    }

    def isValid(code: Short): Boolean = values.map(_.code).contains(code)
  }

  def withNoId
  (
    categoryId: TodoCategory.Id,
    title: String,
    body: String,
    state: State,
    updatedAt: LocalDateTime = NOW,
    createdAt: LocalDateTime = NOW,
  ): WithNoId = Entity.WithNoId(
    Todo(None, categoryId, title, body, state, updatedAt, createdAt)
  )

  def embeddedId
  (
    id: Id,
    categoryId: TodoCategory.Id,
    title: String,
    body: String,
    state: State,
    updatedAt: LocalDateTime = NOW,
    createdAt: LocalDateTime = NOW
  ): EmbeddedId = Entity.EmbeddedId(
    Todo(Some(id), categoryId, title, body, state, updatedAt, createdAt)
  )

}