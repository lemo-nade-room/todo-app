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
  val categoryApplicationService: CategoryApplicationService
) extends BaseController {

  def create: Action[AnyContent] = Action.async { implicit req =>
    val createCategory = CategoryForm.createForm.bindFromRequest().get
    for (_ <- categoryApplicationService.create(createCategory)) yield Redirect("/")
  }

  def update: Action[AnyContent] = Action.async { implicit req =>
    val updateCategory = CategoryForm.updateForm.bindFromRequest().get
    for (_ <- categoryApplicationService.update(updateCategory)) yield Redirect("/")
  }

  def delete(): Action[AnyContent] = Action.async { implicit req =>
    val deleteCategory = CategoryForm.deleteForm.bindFromRequest().get
    for (_ <- categoryApplicationService.delete(deleteCategory)) yield Redirect("/")
  }
}
