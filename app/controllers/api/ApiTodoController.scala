package controllers.api

import javax.inject._
import play.api.mvc._
import repository.{CategoryRepository, TodoRepository}
import play.api.libs.json._
import content.api._
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

  def create: Action[JsValue] = Action(parse.json).async { implicit req =>
    req.body.validate[content.api.todo.Create].fold(
      error => Future.successful(BadRequest(error.toString)),
      create => todoRepository.create(create.value).map(_ => Created)
    )
  }

  def update: Action[JsValue] = Action(parse.json).async { implicit req =>
    req.body.validate[content.api.todo.Update].fold(
      error => Future.successful(BadRequest(error.toString)),
      update => todoRepository.update(update.value).map(_ => Ok)
    )
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit req =>
    println(id)
    todoRepository.delete(Todo.Id(id.asInstanceOf[Todo.Id.U]))
      .map(_ => Ok)
  }
}
