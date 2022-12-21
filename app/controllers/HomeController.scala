package controllers

import javax.inject._
import play.api.mvc._
import model.ViewValueHome
import service.CategoryApplicationService
import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@Singleton
class HomeController @Inject()
(
  val controllerComponents: ControllerComponents,
  val categoryApplicationService: CategoryApplicationService,
) extends BaseController {

  def index: Action[AnyContent] = Action.async { implicit req => Future {
    val categories = Await.result(categoryApplicationService.views(), Duration(10, TimeUnit.SECONDS))
    val vv = ViewValueHome(
      title = "Home",
      cssSrc = Seq("main.css"),
      jsSrc = Seq("main.js"),
      categories
    )
    Ok(views.html.Home(vv))
  }}
}
