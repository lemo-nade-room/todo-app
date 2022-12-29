package repository

import com.google.inject.ImplementedBy
import database.repository.DatabaseCategoryRepository
import model.{Todo, TodoCategory}
import scala.concurrent.Future

@ImplementedBy(classOf[DatabaseCategoryRepository])
trait CategoryRepository {
  def all(): Future[Seq[TodoCategory#EmbeddedId]]

  /** @return 全てのカテゴリとTodo */
  def allWithTodos(): Future[Seq[(Option[Todo#EmbeddedId], TodoCategory#EmbeddedId)]]

  def create(category: TodoCategory#WithNoId): Future[Unit]

  def update(category: TodoCategory#EmbeddedId): Future[Unit]

  /** 指定されたカテゴリIDのカテゴリをそのカテゴリに所属するTODOごと削除 */
  def delete(id: TodoCategory.Id): Future[Unit]

  def findBySlug(slug: String): Future[Option[TodoCategory#EmbeddedId]]

}
