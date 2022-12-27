package model.database.repository

import model.database.ixiasmodel.TodoModel
import model.database.ixiasrepository.IxiasTodoRepository
import model.repository.TodoRepository
import model.entity.Todo
import model.entity.todo.{TodoBody, TodoCategory, TodoID, TodoState, TodoTitle}
import slick.jdbc.MySQLProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class DatabaseTodoRepository() extends TodoRepository {

  val repository: IxiasTodoRepository[MySQLProfile.type] = IxiasTodoRepository()(MySQLProfile)

  /**
   * 新規Todoを追加する
   *
   * @return 作成されたTodoのID
   */
  override def create(title: TodoTitle, body: TodoBody, state: TodoState, category: TodoCategory): Future[TodoID] = {
    val newTodoModel = TodoModel.build(category.id, title, body, state)
    for (id <- repository.add(newTodoModel)) yield new TodoID(id.longValue())
  }

  /** Todoの内容を上書きする */
  override def update(id: TodoID, title: TodoTitle, body: TodoBody, state: TodoState, category: TodoCategory): Future[Unit] = {
    val newTodo = TodoModel.build(id, category.id, title, body, state)
    repository.update(newTodo).map(_ => Unit)
  }

  /** @return 全てのTodoを取得する */
  override def all(): Future[Seq[Todo]] = repository.all()

  override def delete(id: TodoID): Future[Unit] = {
    repository.remove(TodoModel.id(id)).map(_ => Unit)
  }

  override def find(id: TodoID): Future[Option[Todo]] = repository.find(id)
}
