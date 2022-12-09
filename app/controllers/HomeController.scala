/**
 *
 * to do sample project
 *
 */

package controllers

import javax.inject._
import play.api.mvc._
import model.ViewValueHome
import model.database.table.TodoCategoryTable
import slick.lifted.TableQuery
import model.database.{Connection, Table}
import slick.jdbc.MySQLProfile.api._

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Success

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index(): Action[AnyContent] = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Home",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    val query = Table.todoCategories.result
    val future = Connection.db.run(query)
    Await.ready(future, Duration(10, TimeUnit.SECONDS))
    val categories = future.value.get.get
    Ok(views.html.Home(vv, categories))
  }
}
