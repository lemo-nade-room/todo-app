package service

import com.google.inject.{ImplementedBy, Inject}
import model.{Todo, TodoCategory}
import repository.TodoRepository
import scala.concurrent.Future

@ImplementedBy(classOf[TodoStateServiceImpl])
trait TodoStateService {

  /**
   * 指定された条件にマッチするページのtodo
   *
   * @param categoryId  カテゴリID
   * @param state todoのstate
   * @param page  1以上
   * @return マッチするものがなければ空配列を返す
   */
  def fetchTodos(categoryId: TodoCategory.Id, state: Todo.State, page: Int): Future[Seq[Todo#EmbeddedId]]
}

object TodoStateService {
  val countPerPage = 20
}

case class TodoStateServiceImpl @Inject()
(
  todoRepository: TodoRepository
) extends TodoStateService {
  def fetchTodos
  (
    categoryId: TodoCategory.Id,
    state: Todo.State,
    page: Int
  ): Future[Seq[Todo#EmbeddedId]] =
    todoRepository.todosSortDeskUpdated(categoryId, state, limit, offset(page))

  private def limit = TodoStateService.countPerPage

  private def offset(page: Int) = TodoStateService.countPerPage * (page - 1)
}
