package entity

import entity.TodoOwnerState.isValid
import model.Todo

/**
 * Todoをもつ進捗状態のエンティティ。
 * todosは更新日が新しい順に並ぶ。
 */
case class TodoOwnerState(state: Todo.State, todos: Seq[Todo.EmbeddedId]) {
  if (!isValid(state, todos)) {
    throw new IllegalArgumentException()
  }
}

object TodoOwnerState {

  private def isValid(state: Todo.State, todos: Seq[Todo.EmbeddedId]) = {
    isSortedTodos(todos) && isAllTodoHasEqualState(state, todos)
  }

  private def isSortedTodos(todos: Seq[Todo.EmbeddedId]): Boolean = {
    (1 until todos.length).forall { i =>
      todos(i - 1).v.updatedAt.isAfter(todos(i).v.updatedAt)
    }
  }

  private def isAllTodoHasEqualState(state: Todo.State, todos: Seq[Todo.EmbeddedId]) = {
    todos.forall(_.v.state.code == state.code)
  }
}