package controllers

import form.CategoryForm

import javax.inject._
import play.api.mvc._
import model.repository.CategoryRepository

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class CategoryController @Inject()
(
  val controllerComponents: ControllerComponents,
  val categoryRepository: CategoryRepository,
) extends BaseController {

  def create: Action[AnyContent] = Action.async { implicit req =>
    val createCategory = CategoryForm.createForm.bindFromRequest().get
    for {
      _ <- categoryRepository.create(createCategory)
    } yield Redirect("/")
  }

  def update: Action[AnyContent] = Action.async { implicit req =>
    val updateCategory = CategoryForm.updateForm.bindFromRequest().get
    for {
      _ <- categoryRepository.update(updateCategory)
    } yield Redirect("/")
  }

  def delete(): Action[AnyContent] = Action.async { implicit req =>
    val deleteCategory = CategoryForm.deleteForm.bindFromRequest().get
    for {
      _ <- categoryRepository.delete(deleteCategory.id)
    } yield Redirect("/")
  }
}
