package model.database.model

import java.sql.Timestamp


final case class TodoCategoryModel
(
  id: Long,
  name: String,
  slug: String,
  color: Int,
  updatedAt: Timestamp,
  createdAt: Timestamp,
)