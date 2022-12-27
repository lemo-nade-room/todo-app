package service

import com.google.inject.{ImplementedBy, Inject}
import model.content.TodoContent
import model.entity.todo.category.CategoryID
import model.entity.todo.{TodoBody, TodoID, TodoState, TodoTitle}
import model.repository.{CategoryRepository, TodoRepository}
import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@ImplementedBy(classOf[TodoApplicationServiceImpl])
trait TodoApplicationService {
  def create(create: TodoContent.Create): Future[Unit]

  def update(update: TodoContent.Update): Future[Unit]

  def delete(delete: TodoContent.Delete): Future[Unit]
}

private case class TodoApplicationServiceImpl @Inject()
(
  todoRepository: TodoRepository,
  categoryRepository: CategoryRepository,
) extends TodoApplicationService {

  override def create(create: TodoContent.Create): Future[Unit] = Future {
    val title = new TodoTitle(create.title)
    val body = TodoBody(create.body)
    val categoryId = CategoryID(create.categoryId)
    val category = Await.result(categoryRepository.find(categoryId), Duration(10, TimeUnit.SECONDS)) match {
      case Some(category) => category
      case None => throw new IllegalArgumentException(s"$categoryId is not found")
    }

    Await.result(todoRepository.create(title, body, TodoState.init, category).map(_ => Unit), Duration(10, TimeUnit.SECONDS))
  }

  override def update(update: TodoContent.Update): Future[Unit] = Future {
    val id = new TodoID(update.todoId)
    val title = new TodoTitle(update.title)
    val body = TodoBody(update.body)
    val state = TodoState(update.state)
    val categoryId = CategoryID(update.categoryId)
    val category = Await.result(categoryRepository.find(categoryId), Duration(10, TimeUnit.SECONDS)) match {
      case Some(category) => category
      case None => throw new IllegalArgumentException(s"$categoryId is not found")
    }

    Await.result(todoRepository.update(id, title, body, state, category), Duration(10, TimeUnit.SECONDS))
  }

  override def delete(delete: TodoContent.Delete): Future[Unit] = {
    val id = new TodoID(delete.id)
    todoRepository.delete(id)
  }
}
