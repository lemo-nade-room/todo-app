package model.entity

import model.entity.todo.{TodoBody, TodoCategory, TodoID, TodoState, TodoTitle}

import java.time.LocalDateTime

case class Todo
(
  id: TodoID,
  category: TodoCategory,
  title: TodoTitle,
  body: TodoBody,
  state: TodoState,
  createdAt: LocalDateTime,
  updatedAt: LocalDateTime,
)
