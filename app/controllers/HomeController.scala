package controllers

import javax.inject._
import play.api.mvc._
import model.ViewValueHome
import model.database.repository.DatabaseCategoryRepository
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
}
