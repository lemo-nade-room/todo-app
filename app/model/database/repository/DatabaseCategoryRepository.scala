package model.database.repository

import scala.concurrent.Future
import model.database.ixiasrepository.IxiasCategoryRepository
import model.entity.todo.TodoCategory
import model.entity.todo.category.{CategoryColor, CategoryID, CategoryName, CategorySlug}
import model.repository.CategoryRepository
import slick.jdbc.MySQLProfile

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
  override def create(name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[CategoryID] =
    repository.create(name, slug, color)

  override def update(id: CategoryID, name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[Unit] =
    repository.update(id, name, slug, color)

  /** 指定されたカテゴリIDのカテゴリをそのカテゴリに所属するTODOごと削除 */
  override def delete(id: CategoryID): Future[Unit] = repository.delete(id)

  /** 指定されたカテゴリIDのカテゴリ */
  override def find(id: CategoryID): Future[Option[TodoCategory]] = repository.find(id)
}
