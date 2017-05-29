package com.xebia.doobie

import com.xebia.doobie.common.PgConnection

object HelloDoobie extends App {

  import doobie.imports._
  import scalaz._, Scalaz._
  import scalaz.concurrent.Task

  val xa = PgConnection.connection

  val program = sql"select 42".query[Int].unique

  private val result = program.transact(xa).run

  println(s"The result is <$result>")
}
