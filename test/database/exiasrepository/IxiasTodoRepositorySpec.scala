package database.exiasrepository

import database.ixiasrepository.{IxiasCategoryRepository, IxiasTodoRepository}
import model.{Todo, TodoCategory}
import org.specs2.mutable.Specification
import org.specs2.specification.{AfterAll, BeforeAll}
import slick.jdbc.MySQLProfile
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class IxiasTodoRepositorySpec extends Specification with BeforeAll with AfterAll {

  private val categoryRepository: IxiasCategoryRepository[MySQLProfile.type] = IxiasCategoryRepository()(MySQLProfile)
  private val todoRepository: IxiasTodoRepository[MySQLProfile.type] = IxiasTodoRepository()(MySQLProfile)

  private var categoryId1: TodoCategory.Id = TodoCategory.Id(0L.asInstanceOf[TodoCategory.Id.U])
  private var categoryId2: TodoCategory.Id = TodoCategory.Id(0L.asInstanceOf[TodoCategory.Id.U])

  override def beforeAll(): Unit = {
    val category1 = TodoCategory.withNoId("テスト用カテゴリ1", "test1", 1)
    categoryId1 = Await.result(categoryRepository.add(category1), Duration(10, TimeUnit.SECONDS))


    val category2 = TodoCategory.withNoId("テスト用カテゴリ2", "test2", 2)
    categoryId2 = Await.result(categoryRepository.add(category2), Duration(10, TimeUnit.SECONDS))

    // バルクインサートがリポジトリ機能に追加されれば変更したいが、新しくバルクインサート機能をつける必要もなかったため
    for (todo <- todos) todoRepository.add(todo)
  }

  override def afterAll: Unit = {
    Await.ready(categoryRepository.remove(categoryId1), Duration(10, TimeUnit.SECONDS))
    Await.ready(categoryRepository.remove(categoryId2), Duration(10, TimeUnit.SECONDS))
  }

  "Ixias TodoRepository" should {
    "状態1のカテゴリ1のtodoを更新日が新しい順に全て取得できる" in {
      val results = Await.result(
        todoRepository.todosSortDeskUpdated(categoryId1, Todo.State(1)), Duration(10, TimeUnit.SECONDS)
      )
      results.length must_=== 3
      results(0).v.title must_=== "カテゴリ1-3日-1"
      results(1).v.title must_=== "カテゴリ1-2日-1"
      results(2).v.title must_=== "カテゴリ1-1日-1"
    }
  }

  private def todos = Seq(
    Todo.withNoId(
      categoryId1,
      "カテゴリ1-1日-1",
      "",
      Todo.State(1),
      LocalDateTime.of(2022, 1, 1, 1, 1, 1),
      LocalDateTime.of(2022, 1, 1, 1, 1, 1),
    ),
    Todo.withNoId(
      categoryId1,
      "カテゴリ1-3日-1",
      "",
      Todo.State(1),
      LocalDateTime.of(2022, 1, 3, 1, 1, 1),
      LocalDateTime.of(2022, 1, 1, 1, 1, 1),
    ),
    Todo.withNoId(
      categoryId2,
      "カテゴリ2-2日-1",
      "",
      Todo.State(1),
      LocalDateTime.of(2022, 1, 2, 1, 1, 1),
      LocalDateTime.of(2022, 1, 1, 1, 1, 1),
    ),
    Todo.withNoId(
      categoryId1,
      "カテゴリ1-2日-1",
      "",
      Todo.State(1),
      LocalDateTime.of(2022, 1, 2, 1, 1, 1),
      LocalDateTime.of(2022, 1, 1, 1, 1, 1),
    ),
    Todo.withNoId(
      categoryId1,
      "カテゴリ1-3日-2",
      "",
      Todo.State(2),
      LocalDateTime.of(2022, 1, 3, 1, 1, 1),
      LocalDateTime.of(2022, 1, 1, 1, 1, 1),
    ),
  )
}
