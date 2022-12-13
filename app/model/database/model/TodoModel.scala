package model.database.model

import model.content.{TodoContent, TodoStateContent}

import java.sql.Timestamp

case class TodoModel
(
  id: Long,
  categoryId: Long,
  title: String,
  body: String,
  state: Int,
  updatedAt: Timestamp,
  createdAt: Timestamp,
) {
  def convert: TodoContent = TodoContent(id, title, body, TodoStateContent(state))
}