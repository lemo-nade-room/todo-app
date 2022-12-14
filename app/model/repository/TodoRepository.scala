package model.repository

import model.content.TodoContent

import scala.concurrent.Future

trait TodoRepository {
  /**
   * 新規Todoを追加する
   * @return 作成されたTodoのID
   */
  def create(todo: TodoContent.Create): Future[Long]

  /** Todoの内容を上書きする */
  def update(todo: TodoContent.Update): Future[Unit]

  /** @return 全てのTodoを取得する */
  def all(): Future[Seq[TodoContent.View]]

  def delete(todoId: Long): Future[Unit]

}
