package model.database.model

import model.entity.todo.TodoCategory
import model.entity.todo.category.{CategoryColor, CategoryID, CategoryName, CategorySlug}

import java.sql.Timestamp
import java.time.LocalDateTime


case class TodoCategoryModel
(
  id: Long,
  name: String,
  slug: String,
  color: Int,
  updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
  createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
) {
  def category: TodoCategory = TodoCategory(
    CategoryID(id),
    CategoryName(name),
    CategorySlug(slug),
    CategoryColor(color),
    createdAt.toLocalDateTime,
    updatedAt.toLocalDateTime,
  )
}