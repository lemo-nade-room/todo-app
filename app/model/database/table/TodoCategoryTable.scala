package model.database.table

import model.database.model.TodoCategoryModel
import java.sql.Timestamp
import slick.jdbc.MySQLProfile.api._

class TodoCategoryTable(tag: Tag) extends Table[TodoCategoryModel](tag, "to_do_category") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.Unique)

  def slug = column[String]("slug")

  def color = column[Int]("color")

  def updatedAt = column[Timestamp]("updated_at")

  def createdAt = column[Timestamp]("created_at")

  def * = (id, name, slug, color, updatedAt, createdAt) <> ((TodoCategoryModel.apply _).tupled, TodoCategoryModel.unapply)
}