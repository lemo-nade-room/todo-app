package database.repository

import database.ixiasrepository.IxiasTodoRepository
import model.Todo
import model.entity.Todo
import model.entity.todo.{TodoBody, TodoCategory, TodoID, TodoState, TodoTitle}
import repository.TodoRepository
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
    val newTodoModel = Todo.build(category.id, title, body, state)
    for (id <- repository.add(newTodoModel)) yield new TodoID(id.longValue())
  }

  /** Todoの内容を上書きする */
  override def update(id: TodoID, title: TodoTitle, body: TodoBody, state: TodoState, category: TodoCategory): Future[Unit] = {
    val newTodo = Todo.build(id, category.id, title, body, state)
    repository.update(newTodo).map(_ => Unit)
  }

  /** @return 全てのTodoを取得する */
  override def all(): Future[Seq[Todo]] = repository.all()

  override def delete(id: TodoID): Future[Unit] = {
    repository.remove(Todo.id(id)).map(_ => Unit)
  }

  override def find(id: TodoID): Future[Option[Todo]] = repository.find(id)
}
