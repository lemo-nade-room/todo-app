package model.repository

import com.google.inject.ImplementedBy
import model.content.TodoContent
import model.database.repository.DatabaseTodoRepository
import scala.concurrent.Future

@ImplementedBy(classOf[DatabaseTodoRepository])
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
