package controllers

import javax.inject._
import play.api.mvc._
import model.ViewValueHome
import model.content.TodoContent
import model.repository.{CategoryRepository, TodoRepository}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()
(
  val controllerComponents: ControllerComponents,
  val categoryRepository: CategoryRepository,
  val todoRepository: TodoRepository
) extends BaseController {

  def index: Action[AnyContent] = Action.async { implicit req =>
    val vv = ViewValueHome(
      title = "Home",
      cssSrc = Seq("main.css"),
      jsSrc = Seq("main.js")
    )
     categoryRepository.all()
       .map(categories => Ok(views.html.Home(vv, categories)))
  }

  def create: Action[AnyContent] = Action.async { implicit req =>
    val createTodo = TodoContent.createForm.bindFromRequest().get
    for {
      _ <- todoRepository.create(createTodo)
      result <- index(req)
    } yield result
  }

  def update: Action[AnyContent] = Action.async { implicit req =>
    val updateTodo = TodoContent.updateForm.bindFromRequest().get
    for {
      _ <- todoRepository.update(updateTodo)
      result <- index(req)
    } yield result
  }

  def delete(todoId: Long): Action[AnyContent] = Action.async { implicit req =>
    for {
      _ <- todoRepository.delete(todoId)
      result <- index(req)
    } yield result
  }
}
