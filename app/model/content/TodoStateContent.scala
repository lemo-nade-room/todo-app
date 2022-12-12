package model.content

import model.content.TodoStateContent.isValid

case class TodoStateContent(state: Int) {

  if (!isValid(state)) throw throw new IllegalArgumentException(s"$state is not in 0...2")
  def display: String = state match {
    case 0 => "TODO(着手前)"
    case 1 => "進行中"
    case 2 => "完了"
  }
}

object TodoStateContent {
  private val MIN = 0
  private val MAX = 2
  def isValid(state: Int): Boolean = MIN <= state && state <= MAX
}