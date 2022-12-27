package model.entity.todo.category

class CategoryName(val value: String) extends AnyVal with Ordered[CategoryName] {
  override def compare(that: CategoryName): Int = value.compare(that.value)
}
