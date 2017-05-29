package com.xebia.doobie.query

import com.xebia.doobie.common.PgConnection

object MonadOrFunctor extends App {

  import doobie.imports._
  import scalaz._
  import Scalaz._

  val xa = PgConnection.connection

  val largerProgram = for {
    a <- sql"select 42".query[Int].unique
    b <- sql"select power(5, 2)".query[Int].unique
  } yield (a + b)
  val firstResult = largerProgram.transact(xa).run
  println(s"<$firstResult> is the first result")


  val oneProgram = sql"select 42".query[Int].unique
  val anotherProgram = sql"select power(5, 2)".query[Int].unique
  val secondResult = (oneProgram |@| anotherProgram) {
    _ + _
  }.transact(xa).run

  println(s"<$secondResult> is the second result")

}
