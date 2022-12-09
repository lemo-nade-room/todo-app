package model.database.model

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
)