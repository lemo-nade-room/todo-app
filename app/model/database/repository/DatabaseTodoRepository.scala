package model.database.repository

import model.database.ixiasrepository.IxiasTodoRepository
import model.repository.TodoRepository
import model.entity.Todo
import model.entity.todo.{TodoBody, TodoCategory, TodoID, TodoState, TodoTitle}
import slick.jdbc.MySQLProfile
import scala.concurrent.Future

case class DatabaseTodoRepository() extends TodoRepository {

  val repository: IxiasTodoRepository[MySQLProfile.type] = IxiasTodoRepository()(MySQLProfile)

  /**
   * 新規Todoを追加する
   *
   * @return 作成されたTodoのID
   */
  override def create(title: TodoTitle, body: TodoBody, state: TodoState, category: TodoCategory): Future[TodoID] = {
    repository.create(title, body, state, category)
  }

  /** Todoの内容を上書きする */
  override def update(id: TodoID, title: TodoTitle, body: TodoBody, state: TodoState, category: TodoCategory): Future[Unit] =
    repository.update(id, title, body, state, category)

  /** @return 全てのTodoを取得する */
  override def all(): Future[Seq[Todo]] = repository.all()

  override def delete(id: TodoID): Future[Unit] = repository.delete(id)

  override def find(id: TodoID): Future[Option[Todo]] = repository.find(id)
}
