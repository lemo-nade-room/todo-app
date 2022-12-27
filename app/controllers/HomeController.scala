package controllers

import javax.inject._
import play.api.mvc._
import model.ViewValueHome
import play.api.data.FormError
import service.CategoryApplicationService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class HomeController @Inject()
(
  val controllerComponents: ControllerComponents,
  val categoryApplicationService: CategoryApplicationService,
) extends BaseController {

  def index: Action[AnyContent] = Action.async { implicit req =>
    homeView(Ok)
  }

  def homeView(status: play.api.mvc.Results#Status, errors: Seq[FormError] = Nil): Future[Result] = categoryApplicationService
    .views()
    .map(categories => status.apply(views.html.Home(ViewValueHome(
      title = "Home",
      cssSrc = Seq("main.css"),
      jsSrc = Seq("main.js"),
      categories = categories,
      errors = errors,
    ))))
}
