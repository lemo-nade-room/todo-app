package controllers

import javax.inject._
import play.api.mvc._
import model.ViewValueHome
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
}
