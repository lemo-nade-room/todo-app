package controllers

import javax.inject._
import play.api.mvc._
import model.ViewValueHome
import model.content.TodoContent.{createForm, updateForm}
import model.database.repository.{DatabaseCategoryRepository, DatabaseTodoRepository}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index: Action[AnyContent] = Action.async { implicit req =>
    val vv = ViewValueHome(
      title = "Home",
      cssSrc = Seq("main.css"),
      jsSrc = Seq("main.js")
    )
    DatabaseCategoryRepository().all()
      .map(categories => Ok(views.html.Home(vv, categories)))
  }

  def create: Action[AnyContent] = Action.async { implicit req =>
    val createTodo = createForm.bindFromRequest().get
    for {
      _ <- DatabaseTodoRepository().create(createTodo)
      result <- index(req)
    } yield result
  }

  def update: Action[AnyContent] = Action.async { implicit req =>
    val updateTodo = updateForm.bindFromRequest().get
    for {
      _ <- DatabaseTodoRepository().update(updateTodo)
      result <- index(req)
    } yield result
  }

  def delete(todoId: Long): Action[AnyContent] = Action.async { implicit req =>
    for {
      _ <- DatabaseTodoRepository().delete(todoId)
      result <- index(req)
    } yield result
  }
}
