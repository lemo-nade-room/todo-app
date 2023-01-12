package service

import model.{Todo, TodoCategory}
import repository.{CategoryRepository, TodoRepository}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait TodoStateService {

  /**
   * 指定された条件にマッチするページのtodo
   *
   * @param slug  カテゴリのslug
   * @param state todoのstate
   * @param page  1以上
   * @return マッチするものがなければ空配列を返す
   */
  def fetchTodos(slug: String, state: Todo.State, page: Int): Future[Seq[Todo#EmbeddedId]]
}

object TodoStateService {
  val countPerPage = 20
}

case class TodoStateServiceImpl
(
  categoryRepository: CategoryRepository,
  todoRepository: TodoRepository
) extends TodoStateService {
  override def fetchTodos
  (
    slug: String,
    state: Todo.State,
    page: Int
  ): Future[Seq[Todo#EmbeddedId]] = {
    categoryRepository.findBySlug(slug).flatMap(
      _.map { category => fetchTodosByCategoryId(category.id, state, page) }
        .getOrElse(Future.successful(Nil))
    )
  }

  private def fetchTodosByCategoryId
  (
    id: TodoCategory.Id,
    state: Todo.State,
    page: Int
  ): Future[Seq[Todo#EmbeddedId]] =
    todoRepository.todosSortDeskUpdated(id, state, limit, offset(page))

  private def limit = TodoStateService.countPerPage

  private def offset(page: Int) = TodoStateService.countPerPage * (page - 1)
}
