package controllers

import form.TodoForm
import javax.inject._
import play.api.mvc._
import service.TodoApplicationService
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TodoController @Inject()
(
  val controllerComponents: ControllerComponents,
  val todoApplicationService: TodoApplicationService,
  val homeController: HomeController,
) extends BaseController {

  def create: Action[AnyContent] = Action.async { implicit req =>
    TodoForm.createForm.bindFromRequest().fold(
      error => homeController.homeView(BadRequest, error.errors),
      createTodo => {
        for (_ <- todoApplicationService.create(createTodo)) yield Redirect("/")
      }
    )
  }

  def update: Action[AnyContent] = Action.async { implicit req =>
    TodoForm.updateForm.bindFromRequest().fold(
      error => homeController.homeView(BadRequest, error.errors),
      updateTodo => {
        for (_ <- todoApplicationService.update(updateTodo)) yield Redirect("/")
      }
    )
  }

  def delete(): Action[AnyContent] = Action.async { implicit req =>
    TodoForm.deleteForm.bindFromRequest().fold(
      error => homeController.homeView(BadRequest, error.errors),
      deleteTodo => {
        for (_ <- todoApplicationService.delete(deleteTodo)) yield Redirect("/")
      }
    )
  }
}
