package database.repository

import database.ixiasrepository.IxiasCategoryRepository
import model.{Todo, TodoCategory}
import model.TodoCategory.Id
import scala.concurrent.Future
import repository.CategoryRepository
import slick.jdbc.MySQLProfile
import scala.concurrent.ExecutionContext.Implicits.global

case class DatabaseCategoryRepository() extends CategoryRepository {

  private val repository: IxiasCategoryRepository[MySQLProfile.type] = IxiasCategoryRepository()(MySQLProfile)

  override def all(): Future[Seq[TodoCategory#EmbeddedId]] = repository.all()

  override def allWithTodos(): Future[Seq[(Option[Todo#EmbeddedId], TodoCategory#EmbeddedId)]] =
    repository.allWithTodos()

  override def create(category: TodoCategory#WithNoId): Future[Unit] =
    repository.add(category).map(_ => ())

  override def update(category: TodoCategory#EmbeddedId): Future[Unit] =
    repository.update(category).map(_ => ())

  override def delete(id: Id): Future[Unit] = repository.delete(id)

  override def findBySlug(slug: String): Future[Option[TodoCategory#EmbeddedId]] = repository.findBySlug(slug)
}
