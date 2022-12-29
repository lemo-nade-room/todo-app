package controllers.api

import content.api.ApiCategoryContent
import model.TodoCategory
import javax.inject._
import play.api.mvc._
import repository.CategoryRepository
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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

  def create: Action[JsValue] = Action(parse.json).async { implicit req =>
    req.body.validate[ApiCategoryContent.Create].fold(
      error => Future.successful(BadRequest(error.toString)),
      create => categoryRepository.create(create.value).map(_ => Created)
    )
  }

  def update: Action[JsValue] = Action(parse.json).async { implicit req =>
    req.body.validate[ApiCategoryContent.Update].fold(
      error => Future.successful(BadRequest(error.toString)),
      update => categoryRepository.update(update.value).map(_ => Ok)
    )
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit req =>
    categoryRepository.delete(TodoCategory.Id(id.asInstanceOf[TodoCategory.Id.U]))
      .map(_ => Ok)
  }

}
