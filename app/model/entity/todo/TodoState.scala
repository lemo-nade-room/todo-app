package model.entity.todo


case class TodoState(value: Int) {

  private val MIN = 0
  private val MAX = 2

  def apply(state: Int): TodoState = {
    if (MIN <= state && state <= MAX) TodoState(state)
    else throw new IllegalArgumentException(s"$state is not in $MIN ... $MAX")
  }
}

object TodoState {
  def init: TodoState = TodoState(0)
}
