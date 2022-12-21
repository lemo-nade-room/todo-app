package model.database.table

import ixias.persistence.model.{DataSourceName, Table}
import model.database.ixiasmodel.{TodoCategoryModel, TodoModel}
import slick.jdbc.JdbcProfile
import java.time.LocalDateTime

case class TodoTable[P <: JdbcProfile]()(implicit val driver: P) extends Table[TodoModel, P] {
  override lazy val dsn: Map[String, DataSourceName] = Map(
    "master" -> DataSourceName("ixias.db.mysql://master/to_do"),
    "slave" -> DataSourceName("ixias.db.mysql://slave/to_do"),
  )

  import api._

  class Query extends BasicQuery(new Table(_))

  override val query = new Query

  class Table(tag: Tag) extends BasicTable(tag, "to_do") {

    import TodoModel._

    def id = column[Id]("id", O.AutoInc, O.PrimaryKey)

    def categoryId = column[TodoCategoryModel.Id]("category_id")

    def title = column[String]("title")

    def body = column[String]("body")

    def state = column[State]("state")

    def updatedAt = column[LocalDateTime]("updated_at")

    def createdAt = column[LocalDateTime]("created_at")

    def * = (id.?, categoryId, title, body, state, updatedAt, createdAt) <> ((TodoModel.apply _).tupled, TodoModel.unapply)

  }
}
