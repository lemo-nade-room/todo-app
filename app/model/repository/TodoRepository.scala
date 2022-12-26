package model.repository

import com.google.inject.ImplementedBy
import model.database.repository.DatabaseTodoRepository
import model.entity.Todo
import model.entity.todo.{TodoBody, TodoCategory, TodoID, TodoState, TodoTitle}
import slick.jdbc.MySQLProfile

import scala.concurrent.Future

@ImplementedBy(classOf[DatabaseTodoRepository])
trait TodoRepository {

  /**
   * 新規Todoを追加する
   * @return 作成されたTodoのID
   */
  def create(title: TodoTitle, body: TodoBody, state: TodoState, category: TodoCategory): Future[Todo]

  /** Todoの内容を上書きする */
  def update(id: TodoID, title: TodoTitle, body: TodoBody, state: TodoState, category: TodoCategory): Future[Unit]

  /** @return 全てのTodoを取得する */
  def all(): Future[Seq[Todo]]

  def delete(id: TodoID): Future[Unit]

  def find(id: TodoID): Future[Option[Todo]]

}
