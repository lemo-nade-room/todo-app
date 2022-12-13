package controllers

import javax.inject._
import play.api.mvc._
import model.ViewValueHome
import model.database.repository.{DatabaseCategoryRepository, DatabaseTodoRepository}
import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, number, text}

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index(): Action[AnyContent] = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Home",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    val categories = Await.result(DatabaseCategoryRepository().all(), Duration(10, TimeUnit.SECONDS))
    Ok(views.html.Home(vv, categories))
  }

  // TODO: 新規作成はできるがindexのViewが返せない
  def create(): Action[AnyContent] = Action { implicit req =>
    case class CreateTodo(title: String, body: String, categoryId: Long)
    val form = Form(
      mapping(
        "title" -> text,
        "body" -> text,
        "categoryId" -> longNumber
      )(CreateTodo.apply)(CreateTodo.unapply)
    )
    val create = form.bindFromRequest().get
    Await.ready(DatabaseTodoRepository().create(create.categoryId, create.title, create.body), Duration(10, TimeUnit.SECONDS))
    return index()
  }
}
