package controllers

import form.CategoryForm
import javax.inject._
import play.api.mvc._
import service.CategoryApplicationService
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class CategoryController @Inject()
(
  val controllerComponents: ControllerComponents,
  val categoryApplicationService: CategoryApplicationService,
  val homeController: HomeController,
) extends BaseController {

  def create: Action[AnyContent] = Action.async { implicit req =>
    CategoryForm.createForm.bindFromRequest().fold(
      error => homeController.homeView(BadRequest, error.errors),
      createCategory => {
        for (_ <- categoryApplicationService.create(createCategory)) yield Redirect("/")
      }
    )
  }

  def update: Action[AnyContent] = Action.async { implicit req =>
    CategoryForm.updateForm.bindFromRequest().fold(
      error => homeController.homeView(BadRequest, error.errors),
      updateCategory => {
        for (_ <- categoryApplicationService.update(updateCategory)) yield Redirect("/")
      }
    )
  }

  def delete(): Action[AnyContent] = Action.async { implicit req =>
    CategoryForm.deleteForm.bindFromRequest().fold(
      error => homeController.homeView(BadRequest, error.errors),
      deleteCategory => for (_ <- categoryApplicationService.delete(deleteCategory)) yield Redirect("/")
    )
  }
}
