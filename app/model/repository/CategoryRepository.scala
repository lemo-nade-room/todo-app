package model.repository

import com.google.inject.ImplementedBy
import model.database.repository.DatabaseCategoryRepository
import model.entity.todo.TodoCategory
import model.entity.todo.category.{CategoryColor, CategoryID, CategoryName, CategorySlug}

import scala.concurrent.Future

@ImplementedBy(classOf[DatabaseCategoryRepository])
trait CategoryRepository {
  /**
   * @return 全てのカテゴリ
   */
  def all(): Future[Seq[TodoCategory]]

  /**
   * カテゴリの新規作成
   * @return 作成されたカテゴリのID
   */
  def create(name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[TodoCategory]

  def update(id: CategoryID, name: CategoryName, slug: CategorySlug, color: CategoryColor): Future[Unit]

  /** 指定されたカテゴリIDのカテゴリをそのカテゴリに所属するTODOごと削除 */
  def delete(id: CategoryID): Future[Unit]

  /** 指定されたカテゴリIDのカテゴリ */
  def find(id: CategoryID): Future[Option[TodoCategory]]


}
