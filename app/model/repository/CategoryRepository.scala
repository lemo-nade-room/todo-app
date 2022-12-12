package model.repository

import model.content.CategoryContent

import scala.concurrent.Future

trait CategoryRepository {
  /**
   * 全てのカテゴリ
   * @return カテゴリは名前順に、todosは更新日順に（新しいもの順に）並ぶ
   */
  def all(): Future[List[CategoryContent]]

}
