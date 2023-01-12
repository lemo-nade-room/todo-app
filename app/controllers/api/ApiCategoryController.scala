package controllers.api

import content.api.ApiCategoryContent
import model.TodoCategory
import javax.inject._
import play.api.mvc._
import repository.CategoryRepository
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ApiCategoryController @Inject()
(
  val controllerComponents: ControllerComponents,
  val categoryRepository: CategoryRepository,
) extends BaseController {

  def index: Action[AnyContent] = Action.async { implicit req =>
    categoryRepository.all()
      .map(ApiCategoryContent.All.build)
      .map(all => Ok(Json.toJson(all)))
  }

  def create: Action[ApiCategoryContent.Create] = Action(parse.json[ApiCategoryContent.Create]).async { implicit req =>
    categoryRepository.create(req.body.value).map(_ => Created)
  }

  def update: Action[ApiCategoryContent.Update] = Action(parse.json[ApiCategoryContent.Update]).async { implicit req =>
    categoryRepository.update(req.body.value).map(_ => Ok)
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit req =>
    categoryRepository
      .delete(TodoCategory.Id(id.asInstanceOf[TodoCategory.Id.U]))
      .map(_ => Ok)
  }

}
