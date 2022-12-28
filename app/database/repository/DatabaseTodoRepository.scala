package database.repository

import database.ixiasrepository.IxiasTodoRepository
import ixias.persistence.dbio.Execution.Implicits.defaultExecutionContext
import model.{Todo, TodoCategory}
import repository.TodoRepository
import slick.jdbc.MySQLProfile
import scala.concurrent.Future

case class DatabaseTodoRepository() extends TodoRepository {

  private val repository: IxiasTodoRepository[MySQLProfile.type] = IxiasTodoRepository()(MySQLProfile)

  override def create(todo: Todo#WithNoId): Future[Unit] = repository.add(todo).map(_ => ())

  override def update(todo: Todo#EmbeddedId): Future[Unit] = repository.update(todo).map(_ => ())

  override def all(): Future[Seq[Todo#EmbeddedId]] = repository.all()

  override def all(categoryId: TodoCategory.Id): Future[Seq[Todo#EmbeddedId]] = repository.all(categoryId)

  override def delete(id: Todo.Id): Future[Unit] = repository.remove(id).map(_ => ())
}
