package model.database.model

import model.content.TodoContent

import java.sql.Timestamp
import java.time.LocalDateTime

case class TodoModel
(
  id: Long = 0,
  categoryId: Long,
  title: String,
  body: String,
  state: Int,
  updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
  createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
) {
  def convertView: TodoContent.View = TodoContent.View(id, title, body, state)
}