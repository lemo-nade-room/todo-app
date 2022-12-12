package model.database

import slick.jdbc.MySQLProfile.api._

import java.util.concurrent.TimeUnit
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.{Failure, Success, Try}
object Connection {
  val db = Database.forConfig("mysql")

  def await[T](future: Future[T]): T = {
    Await.ready(future, Duration(10, TimeUnit.SECONDS))
    future.value.get.get
  }
}
