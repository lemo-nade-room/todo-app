package controllers

import javax.inject._
import play.api.mvc._
import model.ViewValueHome
import model.content.TodoStateContent
import model.database.repository.{DatabaseCategoryRepository, DatabaseTodoRepository}
import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, number, optional, text}

import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

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
    case class CreateTodo(title: String, body: String, categoryId: Long)
    val form = Form(
      mapping(
        "title" -> text,
        "body" -> text,
        "categoryId" -> longNumber
      )(CreateTodo.apply)(CreateTodo.unapply)
    )
    val create = form.bindFromRequest().get
    for {
      _ <- DatabaseTodoRepository().create(create.categoryId, create.title, create.body)
      result <- index(req)
    } yield result
  }

  def update: Action[AnyContent] = Action.async { implicit req =>
    case class UpdateTodo
    (
      todoId: Long,
      title: String,
      body: String,
      state: Int,
      categoryId: Long,
    )

    println(req.body)

    val updateTodo = Form(mapping(
      "todoId" -> longNumber,
      "title" -> text,
      "body" -> text,
      "state" -> number,
      "category" -> longNumber,
    )(UpdateTodo.apply)(UpdateTodo.unapply)).bindFromRequest().get

    for {
      _ <- DatabaseTodoRepository().update(
        updateTodo.todoId,
        updateTodo.title,
        updateTodo.body,
        TodoStateContent(updateTodo.state),
        updateTodo.categoryId
      )
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
