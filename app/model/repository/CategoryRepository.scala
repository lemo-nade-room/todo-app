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
  def all(): Future[Seq[CategoryContent]]

}
