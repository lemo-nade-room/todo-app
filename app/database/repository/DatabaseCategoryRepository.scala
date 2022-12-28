package database.repository

import database.ixiasrepository.IxiasCategoryRepository
import model.TodoCategory
import model.database.TodoCategoryModel

import scala.concurrent.Future
import model.entity.todo.TodoCategory
import model.entity.todo.category.{CategoryColor, CategoryID, CategoryName, CategorySlug}
import repository.CategoryRepository
import slick.jdbc.MySQLProfile

import scala.concurrent.ExecutionContext.Implicits.global

case class DatabaseCategoryRepository() extends CategoryRepository {

  val repository: IxiasCategoryRepository[MySQLProfile.type] = IxiasCategoryRepository()(MySQLProfile)

  /**
   * @return 全てのカテゴリ
   */
  override def all(): Future[Seq[TodoCategory]] = repository.all()

  /**
   * カテゴリの新規作成
   *
   * @return 作成されたカテゴリのID
   */
  override def create(name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[CategoryID] = {
    val newCategoryModel = TodoCategory.build(name, slug, color)
    for (id <- repository.add(newCategoryModel)) yield new CategoryID(id.longValue())
  }

  override def update(id: CategoryID, name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[Unit] = {
    val newCategory = TodoCategory.build(id, name, slug, color)
    repository.update(newCategory).map(_ => Unit)
  }

  /** 指定されたカテゴリIDのカテゴリをそのカテゴリに所属するTODOごと削除 */
  override def delete(id: CategoryID): Future[Unit] = repository.delete(id)

  /** 指定されたカテゴリIDのカテゴリ */
  override def find(id: CategoryID): Future[Option[TodoCategory]] = {
    repository.get(TodoCategory.id(id)).map(_.map(_.v.category))
  }

}
