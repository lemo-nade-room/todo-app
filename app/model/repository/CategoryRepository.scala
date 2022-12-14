package model.repository

import com.google.inject.ImplementedBy
import model.content.CategoryContent
import model.database.repository.DatabaseCategoryRepository
import scala.concurrent.Future

@ImplementedBy(classOf[DatabaseCategoryRepository])
trait CategoryRepository {
  /**
   * 全てのカテゴリ
   * @return カテゴリは名前順に、todosは更新日順に（新しいもの順に）並ぶ
   */
  def all(): Future[Seq[CategoryContent.View]]

  /**
   * カテゴリの新規作成
   * @return 作成されたカテゴリのID
   */
  def create(category: CategoryContent.Create): Future[Long]

  def update(categry: CategoryContent.Update): Future[Unit]

  /** 指定されたカテゴリIDのカテゴリをそのカテゴリに所属するTODOごと削除 */
  def delete(categoryId: Long): Future[Unit]


}
