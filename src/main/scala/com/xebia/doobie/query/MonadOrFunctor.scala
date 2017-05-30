package com.xebia.doobie.query

import com.xebia.doobie.common.PgConnection

object MonadOrFunctor {

  import doobie.imports._
  import scalaz._
  import Scalaz._

  val xa = PgConnection.connection

  // It could be used as a monad
  private val largerProgram = for {
    a <- sql"select 42".query[Int].unique
    b <- sql"select power(5, 2)".query[Int].unique
  } yield (a + b)
  val resultAsMonad = largerProgram.transact(xa).run

  // It could be used as an applicative functor
  val oneProgram = sql"select 42".query[Int].unique
  val anotherProgram = sql"select power(5, 2)".query[Int].unique
  val resultAsFunctor = (oneProgram |@| anotherProgram) {
    _ + _
  }.transact(xa).run

}
