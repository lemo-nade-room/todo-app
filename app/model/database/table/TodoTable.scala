package model.database.table

import model.database.ixiasmodel.TodoModel
import java.sql.Timestamp
import slick.jdbc.MySQLProfile.api._
import slick.lifted.ProvenShape

class TodoTable(tag: Tag) extends Table[TodoModel](tag, "to_do") {

  def id = column[Long]("id", O.AutoInc, O.PrimaryKey)

  def categoryId = column[Long]("category_id")

  def title = column[String]("title")

  def body = column[String]("body")

  def state = column[Short]("state")

  def updatedAt = column[Timestamp]("updated_at")

  def createdAt = column[Timestamp]("created_at")

  override def * : ProvenShape[TodoModel] = (id, categoryId, title, body, state, updatedAt, createdAt) <> ((TodoModel.apply _).tupled, TodoModel.unapply)
}
