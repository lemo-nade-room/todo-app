package database.table

import ixias.persistence.model.{DataSourceName, Table}
import model.TodoCategory
import slick.jdbc.JdbcProfile
import java.time.LocalDateTime

case class TodoCategoryTable[P <: JdbcProfile]()(implicit val driver: P) extends Table[TodoCategory, P] {
  override lazy val dsn: Map[String, DataSourceName] = Map(
    "master" -> DataSourceName("ixias.db.mysql://master/to_do"),
    "slave" -> DataSourceName("ixias.db.mysql://slave/to_do"),
  )

  import api._

  class Query extends BasicQuery(new Table(_))

  override val query = new Query

  class Table(tag: Tag) extends BasicTable(tag, "to_do_category") {

    import TodoCategory._

    def id = column[Id]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name", O.Unique)

    def slug = column[String]("slug")

    def color = column[Int]("color")

    def updatedAt = column[LocalDateTime]("updated_at")

    def createdAt = column[LocalDateTime]("created_at")

    def * = (id.?, name, slug, color, updatedAt, createdAt) <> ((TodoCategory.apply _).tupled, TodoCategory.unapply)

  }
}