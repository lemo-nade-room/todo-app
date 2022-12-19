package model.entity.todo

import model.entity.todo.category.{CategoryColor, CategoryID, CategoryName, CategorySlug}
import java.time.LocalDateTime

case class TodoCategory
(
  id: CategoryID,
  name: CategoryName,
  slug: CategorySlug,
  color: CategoryColor,
  createdAt: LocalDateTime,
  updatedAt: LocalDateTime,
)