package controllers

import form.CategoryForm
import javax.inject._
import play.api.mvc._
import repository.CategoryRepository
import ixias.persistence.dbio.Execution.Implicits.defaultExecutionContext

@Singleton
class CategoryController @Inject()
(
  val controllerComponents: ControllerComponents,
  val categoryRepository: CategoryRepository,
  val homeController: HomeController,
) extends BaseController {

  def create: Action[AnyContent] = Action.async { implicit req =>
    CategoryForm.createForm.bindFromRequest().fold(
      error => homeController.homeView(BadRequest, error.errors),
      create => {
        for (_ <- categoryRepository.create(create.category)) yield Redirect("/")
      }
    )
  }

  def update: Action[AnyContent] = Action.async { implicit req =>
    CategoryForm.updateForm.bindFromRequest().fold(
      error => homeController.homeView(BadRequest, error.errors),
      update => {
        for (_ <- categoryRepository.update(update.category)) yield Redirect("/")
      }
    )
  }

  def delete(): Action[AnyContent] = Action.async { implicit req =>
    CategoryForm.deleteForm.bindFromRequest().fold(
      error => homeController.homeView(BadRequest, error.errors),
      delete => for (_ <- categoryRepository.delete(delete.categoryId)) yield Redirect("/")
    )
  }
}
