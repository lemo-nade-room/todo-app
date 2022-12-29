package controllers.api

import javax.inject._
import play.api.mvc._
import repository.{CategoryRepository, TodoRepository}
import play.api.libs.json._
import content.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ApiTodoController @Inject()
(
  val controllerComponents: ControllerComponents,
  val categoryRepository: CategoryRepository,
  val todoRepository: TodoRepository,
) extends BaseController {

  def index(slug: String): Action[AnyContent] = Action.async { implicit req =>
    categoryRepository.findBySlug(slug)
      .flatMap {
        case Some(category) => todoRepository.all(category.id)
          .map(todos => Ok(Json.toJson(todo.Read.build(category, todos))))
        case None => Future(NotFound)
      }
  }
}
