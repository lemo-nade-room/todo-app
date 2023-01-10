package controllers.api

import javax.inject._
import play.api.mvc._
import repository.{CategoryRepository, TodoRepository}
import play.api.libs.json._
import content.api._
import content.api.todo.{Create, Update}
import model.Todo
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
        case None => Future.successful(NotFound)
      }
  }

  def create: Action[Create] = Action(parse.json[content.api.todo.Create]).async { implicit req =>
    todoRepository.create(req.body.value).map(_ => Created)
  }

  def update: Action[Update] = Action(parse.json[content.api.todo.Update]).async { implicit req =>
    todoRepository.update(req.body.value).map(_ => Ok)
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit req =>
    todoRepository.delete(Todo.Id(id.asInstanceOf[Todo.Id.U]))
      .map(_ => Ok)
  }
}
