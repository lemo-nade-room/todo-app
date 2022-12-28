package service

import com.google.inject.{ImplementedBy, Inject}
import content.CategoryContent
import model.entity.todo.category.{CategoryColor, CategoryID, CategoryName, CategorySlug}
import repository.{CategoryRepository, TodoRepository}
import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@ImplementedBy(classOf[CategoryApplicationServiceImpl])
trait CategoryApplicationService {

  /** カテゴリ名の名前昇順、TODOのupdatedAtの降順 */
  def views(): Future[Seq[CategoryContent.View]]

  def create(create: CategoryContent.Create): Future[Unit]

  def update(update: CategoryContent.Update): Future[Unit]

  def delete(delete: CategoryContent.Delete): Future[Unit]
}

private case class CategoryApplicationServiceImpl @Inject()
(
  todoRepository: TodoRepository,
  categoryRepository: CategoryRepository,
) extends CategoryApplicationService {

  override def views(): Future[Seq[CategoryContent.View]] = Future {
    val categories = Await.result(categoryRepository.all(), Duration(10, TimeUnit.SECONDS)).sortWith(_.name < _.name)
    val todos = Await.result(todoRepository.all(), Duration(10, TimeUnit.SECONDS))

    categories.map(category => CategoryContent.View.make(
      category,
      todos.filter(_.category == category).sortWith((a, b) => a.updatedAt.isAfter(b.updatedAt)))
    )
  }

  override def create(create: CategoryContent.Create): Future[Unit] = {
    val name = new CategoryName(create.name)
    val slug = new CategorySlug(create.slug)
    val color = new CategoryColor(create.color)
    categoryRepository.create(name, slug, color).map(_ => Unit)
  }

  override def update(update: CategoryContent.Update): Future[Unit] = {
    val id = new CategoryID(update.id)
    val name = new CategoryName(update.name)
    val slug = new CategorySlug(update.slug)
    val color = new CategoryColor(update.color)
    categoryRepository.update(id, name, slug, color)
  }

  override def delete(delete: CategoryContent.Delete): Future[Unit] = {
    val id = new CategoryID(delete.id)
    categoryRepository.delete(id)
  }
}
