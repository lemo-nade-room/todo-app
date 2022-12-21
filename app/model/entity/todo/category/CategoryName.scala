package model.entity.todo.category

case class CategoryName(name: String) extends Ordered[CategoryName] {
  override def compare(that: CategoryName): Int = name.compare(that.name)
}
