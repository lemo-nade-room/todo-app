package model.repository

import model.content.{TodoContent, TodoStateContent}

import scala.concurrent.Future

trait TodoRepository {
  /**
   * 新規Todoを追加する
   *
   * @param categoryId カテゴリID
   * @param title      タイトル
   * @param body       本文
   * @return 作成されたTodoのID
   */
  def create(categoryId: Long, title: String, body: String): Future[Long]

  /**
   * Todoの内容を上書きする
   * @param todoId 対象のTodoID
   * @param title タイトルを変更する場合に変更先のタイトル
   * @param body 本文を変更する場合に変更先の本文
   * @param categoryId カテゴリを変更する場合に変更先のカテゴリID
   */
  def update
  (
    todoId: Long,
    title: String,
    body: String,
    state: TodoStateContent,
    categoryId: Long,
  ): Future[Unit]

  /**
   * @return 全てのTodoを取得する
   */
  def all(): Future[Seq[TodoContent]]

  def delete(todoId: Long): Future[Unit]

}
