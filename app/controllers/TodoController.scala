package controllers

import form.TodoForm

import javax.inject._
import play.api.mvc._
import model.repository.{CategoryRepository, TodoRepository}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TodoController @Inject()
(
  val controllerComponents: ControllerComponents,
  val categoryRepository: CategoryRepository,
  val todoRepository: TodoRepository
) extends BaseController {

  def create: Action[AnyContent] = Action.async { implicit req =>
    val createTodo = TodoForm.createForm.bindFromRequest().get
    for (_ <- todoRepository.create(createTodo)) yield Redirect("/")
  }

  def update: Action[AnyContent] = Action.async { implicit req =>
    val updateTodo = TodoForm.updateForm.bindFromRequest().get
    for (_ <- todoRepository.update(updateTodo)) yield Redirect("/")
  }

  def delete(): Action[AnyContent] = Action.async { implicit req =>
    val deleteTodo = TodoForm.deleteForm.bindFromRequest().get
    for (_ <- todoRepository.delete(deleteTodo.id)) yield Redirect("/")
  }
}
