package controllers

import content.CategoryContent
import model.ViewValueHome

import javax.inject._
import play.api.mvc._
import play.api.data.FormError
import scala.concurrent.ExecutionContext.Implicits.global
import repository.CategoryRepository

import scala.concurrent.Future

@Singleton
class HomeController @Inject()
(
  val controllerComponents: ControllerComponents,
  val categoryRepository: CategoryRepository,
) extends BaseController {

  def index: Action[AnyContent] = Action.async { implicit req =>
    homeView(Ok)
  }

  private[controllers] def homeView(status: play.api.mvc.Results#Status, errors: Seq[FormError] = Nil): Future[Result] = {
    categoryRepository.allWithTodos().map(categoryWithTodos =>
      status.apply(views.html.Home(
        ViewValueHome(
          title = "Home",
          cssSrc = Seq("main.css"),
          jsSrc = Seq("main.js"),
          categories = CategoryContent.View.makeViews(categoryWithTodos),
          errors = errors,
        )
      ))
    )
  }
}
