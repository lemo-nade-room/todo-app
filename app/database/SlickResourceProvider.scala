package database

import database.table.{TodoCategoryTable, TodoTable}
import slick.jdbc.JdbcProfile

trait SlickResourceProvider[P <: JdbcProfile] {
  implicit val driver: P
  object TodoTable extends TodoTable
  object TodoCategoryTable extends TodoCategoryTable

  lazy val AllTables = Seq(
    TodoTable,
    TodoCategoryTable
  )

}