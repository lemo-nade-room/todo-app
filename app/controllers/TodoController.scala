package controllers

import form.TodoForm
import javax.inject._
import play.api.mvc._
import repository.TodoRepository
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TodoController @Inject()
(
  val controllerComponents: ControllerComponents,
  var todoRepository: TodoRepository,
  val homeController: HomeController,
) extends BaseController {

  def create: Action[AnyContent] = Action.async { implicit req =>
    TodoForm.createForm.bindFromRequest().fold(
      error => homeController.homeView(BadRequest, error.errors),
      create => {
        for (_ <- todoRepository.create(create.todo)) yield Redirect(routes.HomeController.index())
      }
    )
  }

  def update: Action[AnyContent] = Action.async { implicit req =>
    TodoForm.updateForm.bindFromRequest().fold(
      error => homeController.homeView(BadRequest, error.errors),
      update => {
        for (_ <- todoRepository.update(update.todo)) yield Redirect(routes.HomeController.index())
      }
    )
  }

  def delete(): Action[AnyContent] = Action.async { implicit req =>
    TodoForm.deleteForm.bindFromRequest().fold(
      error => homeController.homeView(BadRequest, error.errors),
      delete => {
        for (_ <- todoRepository.delete(delete.todoId)) yield Redirect(routes.HomeController.index())
      }
    )
  }
}
