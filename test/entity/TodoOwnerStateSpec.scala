package entity

import entity.TodoOwnerStateSpec.{todo1state1, todo1state2, todo2state1, todo3state1}
import model.{Todo, TodoCategory}
import org.specs2.mutable.Specification
import java.time.LocalDateTime

class TodoOwnerStateSpec extends Specification {
  "TodoOwnerState" should {
    "TodoStateとその状態を持つ降順のtodosでインスタンス化できる" in {
      val state = Todo.State(1)
      val todos = Seq(todo3state1, todo2state1, todo1state1)
      val todoOwnerState = TodoOwnerState(state, todos)
      todoOwnerState.state must_=== state
      todoOwnerState.todos must_=== todos
    }

    "降順でないtodosでインスタンス化できない" in {
      val state = Todo.State(1)
      val todos = Seq(todo3state1, todo1state1, todo2state1)
      def initialize = TodoOwnerState(state, todos)
      initialize must throwA[IllegalArgumentException]
    }

    "異なる状態を持つTodoが混ざり込んでいるとインスタンス化できない" in {
      val state = Todo.State(1)
      val todos = Seq(todo3state1, todo2state1, todo1state2)
      def initialize = TodoOwnerState(state, todos)
      initialize must throwA[IllegalArgumentException]
    }
  }
}

object TodoOwnerStateSpec {
  private val categoryId = TodoCategory.Id(2L.asInstanceOf[TodoCategory.Id.U])

  private val todo1state1 = Todo.embeddedId(
    Todo.Id(1L.asInstanceOf[Todo.Id.U]),
    categoryId,
    "タイトル1",
    "本文1",
    Todo.State(1),
    LocalDateTime.of(2023, 1, 1, 5, 6, 8)
  )

  private val todo1state2 = Todo.embeddedId(
    Todo.Id(1L.asInstanceOf[Todo.Id.U]),
    categoryId,
    "タイトル1",
    "本文1",
    Todo.State(2),
    LocalDateTime.of(2023, 1, 1, 5, 6, 8)
  )

  private val todo2state1 = Todo.embeddedId(
    Todo.Id(2L.asInstanceOf[Todo.Id.U]),
    categoryId,
    "タイトル2",
    "本文2",
    Todo.State(1),
    LocalDateTime.of(2023, 1, 2, 5, 6, 8)
  )

  private val todo3state1 = Todo.embeddedId(
    Todo.Id(3L.asInstanceOf[Todo.Id.U]),
    categoryId,
    "タイトル3",
    "本文3",
    Todo.State(1),
    LocalDateTime.of(2023, 1, 3, 5, 6, 8)
  )
}
