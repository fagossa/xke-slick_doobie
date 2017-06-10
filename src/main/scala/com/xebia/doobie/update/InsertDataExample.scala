package com.xebia.doobie.update

import com.xebia.doobie.common.PgConnection
import com.xebia.doobie.update.InsertDataExample.xa
import doobie.imports._

import scalaz._
import Scalaz._

object InsertDataExample extends PersonRepository {

  val xa = PgConnection.connection

  def persistSomeData(): Int = {
    val rows = for {
      row1 <- persist("Alice", Option(12)).run
      row2 <- persist("Bob", None).run
      row3 <- persist("John", Option(17)).run
    } yield row1 + row2 + row3

    val insertedRows = rows.transact(xa).unsafePerformSync
    insertedRows
  }

}

trait PersonRepository {

  def count: Int = {
    sql"select count(name) from person"
      .query[Int]
      .unique
      .transact(xa)
      .run
  }

  def persist(name: String, age: Option[Short]): Update0 =
    sql"insert into person (name, age) values ($name, $age)".update

}
