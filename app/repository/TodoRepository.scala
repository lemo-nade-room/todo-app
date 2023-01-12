package repository

import com.google.inject.ImplementedBy
import database.repository.DatabaseTodoRepository
import model.{Todo, TodoCategory}
import scala.concurrent.Future

@ImplementedBy(classOf[DatabaseTodoRepository])
trait TodoRepository {

  def create(todo: Todo#WithNoId): Future[Unit]

  def update(todo: Todo#EmbeddedId): Future[Unit]

  /** @return 指定されたカテゴリのIdに所属するTodoを全て取得する */
  def all(categoryId: TodoCategory.Id): Future[Seq[Todo#EmbeddedId]]

  /**
   * 指定された条件にマッチするtodoを更新日が新しい順に引き出す
   * @param categoryId カテゴリID
   * @param state 進捗状態
   * @param limit 取得最大個数
   * @param offset 取得する最初のtodoのindex
   */
  def todosSortDeskUpdated
  (
    categoryId: TodoCategory.Id,
    state: Todo.State,
    limit: Int,
    offset: Int,
  ): Future[Seq[Todo#EmbeddedId]]

  def delete(id: Todo.Id): Future[Unit]
}
