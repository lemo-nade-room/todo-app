package model.database.model

import java.sql.Timestamp
import java.time.LocalDateTime


final case class TodoCategoryModel
(
  id: Long,
  name: String,
  slug: String,
  color: Int,
  updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
  createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
)