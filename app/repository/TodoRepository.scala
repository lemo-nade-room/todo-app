package repository

import com.google.inject.ImplementedBy
import database.repository.DatabaseTodoRepository
import model.{Todo, TodoCategory}
import scala.concurrent.Future

@ImplementedBy(classOf[DatabaseTodoRepository])
trait TodoRepository {

  def create(todo: Todo#WithNoId): Future[Unit]

  def update(todo: Todo#EmbeddedId): Future[Unit]

  /** @return 全てのTodoを取得する */
  def all(): Future[Seq[Todo]]

  /** @return 指定されたカテゴリのIdに所属するTodoを全て取得する */
  def all(categoryId: TodoCategory.Id): Future[Seq[Todo]]

  def delete(id: Todo.Id): Future[Unit]
}
