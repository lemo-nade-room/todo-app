package service

import model.{Todo, TodoCategory}
import org.specs2.matcher.describe.Diffable.longDiffable
import org.specs2.mutable.Specification
import repository.{CategoryRepository, TodoRepository}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class TodoStateServiceSpec extends Specification {
  "TodoStateService" should {
    "TodoリポジトリからTodoStateをcode順に並び替えて引き出すことができる" in {
      val todoRepository = new MockTodoRepository()
      val categoryRepository = new FakeCategoryRepository()

      val todoStateService: TodoStateService = TodoStateServiceImpl(categoryRepository, todoRepository)
      val result = Await.result(
        todoStateService.fetchTodos("hello", Todo.State(0), 3),
        Duration(1, TimeUnit.SECONDS)
      )
      result must_=== Nil
      categoryRepository.slug must_=== "hello"
      todoRepository.categoryId must_=== TodoCategory.Id(100L.asInstanceOf[TodoCategory.Id.U])
      todoRepository.state must_=== Todo.State(0)
      todoRepository.limit must_=== 20
      todoRepository.offset must_=== 40
    }
  }
}

class FakeCategoryRepository() extends CategoryRepository {
  override def all(): Future[Seq[TodoCategory#EmbeddedId]] = Future.successful(Nil)

  override def allWithTodos(): Future[Seq[(Option[Todo#EmbeddedId], TodoCategory#EmbeddedId)]] = Future.successful(Nil)

  override def create(category: TodoCategory#WithNoId): Future[Unit] = Future.successful()

  override def update(category: TodoCategory#EmbeddedId): Future[Unit] = Future.successful()

  override def delete(id: TodoCategory.Id): Future[Unit] = Future.successful()

  var slug = ""

  override def findBySlug(slug: String): Future[Option[TodoCategory#EmbeddedId]] = {
    this.slug = slug
    Future.successful(
      Some(
        TodoCategory.embeddedId(
          TodoCategory.Id(100L.asInstanceOf[TodoCategory.Id.U]), "hello", "hello", 1
        )
      )
    )
  }
}

class MockTodoRepository() extends TodoRepository {
  override def create(todo: Todo#WithNoId): Future[Unit] = Future.successful()

  override def update(todo: Todo#EmbeddedId): Future[Unit] = Future.successful()

  override def all(categoryId: TodoCategory.Id): Future[Seq[Todo#EmbeddedId]] = Future.successful(Nil)

  var categoryId: TodoCategory.Id = TodoCategory.Id(0L.asInstanceOf[TodoCategory.Id.U])
  var state: Todo.State = Todo.State(2)
  var limit: Int = -1
  var offset: Int = -1

  override def todosSortDeskUpdated
  (
    categoryId: TodoCategory.Id,
    state: Todo.State,
    limit: Int,
    offset: Int
  ): Future[Seq[Todo#EmbeddedId]] = {
    this.categoryId = categoryId
    this.state = state
    this.limit = limit
    this.offset = offset
    Future.successful(Nil)
  }

  override def delete(id: Todo.Id): Future[Unit] = Future.successful()
}