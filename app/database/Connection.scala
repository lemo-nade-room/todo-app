package database

import slick.jdbc.MySQLProfile.api._

object Connection {
  val db = Database.forConfig("mysql")
}