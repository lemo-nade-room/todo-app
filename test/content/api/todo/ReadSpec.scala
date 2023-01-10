package content.api.todo

import model.{Todo, TodoCategory}
import org.specs2.mutable.Specification
import play.api.libs.json.Json

import java.time.LocalDateTime

class ReadSpec extends Specification {
  "TodoContent Read" should {
    "変換できる" in {
      val categoryId = TodoCategory.Id(1L.asInstanceOf[TodoCategory.Id.U])
      val category = TodoCategory.embeddedId(categoryId, "カテゴリ名", "name", 1)
      val todos = List(
        Todo.embeddedId(
          Todo.Id(1L.asInstanceOf[Todo.Id.U]),
          categoryId,
          "タイトル1",
          "ボディ1",
          Todo.State.of(1),
          LocalDateTime.of(2022, 1, 23, 1, 2, 3),
          LocalDateTime.of(2022, 1, 23, 4, 5, 6),
        ),
        Todo.embeddedId(
          Todo.Id(2L.asInstanceOf[Todo.Id.U]),
          categoryId,
          "タイトル2",
          "ボディ2",
          Todo.State.of(2),
          LocalDateTime.of(2022, 1, 23, 7, 8, 9),
          LocalDateTime.of(2022, 1, 23, 9, 8, 7),
        ),
      )
      val read = Read.build(category, todos)
      val json = Json.toJson(read)
      json must_=== Json.obj(
        "category" -> Json.obj(
          "id" -> 1,
          "name" -> "カテゴリ名",
          "slug" -> "name",
          "color" -> 1,
        ),
        "states" -> Seq(
          Json.obj(
            "state" -> 1,
            "todos" -> Seq(Json.obj(
              "id" -> 1,
              "title" -> "タイトル1",
              "body" -> "ボディ1",
              "date" -> "2022/01/23 01:02:03",
            ))
          ),
        Json.obj(
            "state" -> 2,
            "todos" -> Seq(Json.obj(
              "id" -> 2,
              "title" -> "タイトル2",
              "body" -> "ボディ2",
              "date" -> "2022/01/23 07:08:09",
            ))
          )
        )
      )
    }
  }
}
