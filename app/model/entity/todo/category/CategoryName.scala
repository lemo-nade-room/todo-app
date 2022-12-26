package model.entity.todo.category

case class CategoryName(value: String) extends Ordered[CategoryName] {
  override def compare(that: CategoryName): Int = value.compare(that.value)
}
