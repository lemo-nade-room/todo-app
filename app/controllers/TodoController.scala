package controllers

import form.TodoForm

import javax.inject._
import play.api.mvc._
import model.repository.{CategoryRepository, TodoRepository}
import service.TodoApplicationService

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TodoController @Inject()
(
  val controllerComponents: ControllerComponents,
  val todoApplicationService: TodoApplicationService,
) extends BaseController {

  def create: Action[AnyContent] = Action.async { implicit req =>
    val createTodo = TodoForm.createForm.bindFromRequest().get
    for (_ <- todoApplicationService.create(createTodo)) yield Redirect("/")
  }

  def update: Action[AnyContent] = Action.async { implicit req =>
    val updateTodo = TodoForm.updateForm.bindFromRequest().get
    for (_ <- todoApplicationService.update(updateTodo)) yield Redirect("/")
  }

  def delete(): Action[AnyContent] = Action.async { implicit req =>
    val deleteTodo = TodoForm.deleteForm.bindFromRequest().get
    for (_ <- todoApplicationService.delete(deleteTodo)) yield Redirect("/")
  }
}
