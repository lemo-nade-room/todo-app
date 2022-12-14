package controllers

import javax.inject._
import play.api.mvc._
import model.content.CategoryContent
import model.repository.CategoryRepository
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class CategoryController @Inject()
(
  val controllerComponents: ControllerComponents,
  val categoryRepository: CategoryRepository,
  val homeController: HomeController,
) extends BaseController {

  def create: Action[AnyContent] = Action.async { implicit req =>
    val createCategory = CategoryContent.createForm.bindFromRequest().get
    for {
      _ <- categoryRepository.create(createCategory)
      result <- homeController.index(req)
    } yield result
  }

  def update: Action[AnyContent] = Action.async { implicit req =>
    val updateCategory = CategoryContent.updateForm.bindFromRequest().get
    for {
      _ <- categoryRepository.update(updateCategory)
      result <- homeController.index(req)
    } yield result
  }

  def delete(categoryId: Long): Action[AnyContent] = Action.async { implicit req =>
    for {
      _ <- categoryRepository.delete(categoryId)
      result <- homeController.index(req)
    } yield result
  }
}
