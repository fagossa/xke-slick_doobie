package com.example

object HelloDoobie extends App {

  import doobie.imports._
  import scalaz._, Scalaz._
  import scalaz.concurrent.Task

  val program = sql"select 42".query[Int].unique

  val xa = DriverManagerTransactor[Task](
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://192.168.99.100/postgres",
    user = "postgres", // default user
    pass = "mysecretpassword"
  )

  private val result = program.transact(xa).run
  // 42

  println(s"The result is <$result>")
}
