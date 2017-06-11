package com.xebia.doobie.update

import doobie.imports._
import doobie.util.transactor

import scalaz.concurrent.Task

case class Person(id: String, name: String, age: Option[Int])

object InsertDataExample extends PersonRepository {

  def persistThreeRecords(implicit xa: transactor.Transactor[Task]): Int = {
    val rows = for {
      row1 <- persist("Alice", Option(12)).run
      row2 <- persist("Bob", None).run
      row3 <- persist("John", Option(17)).run
    } yield row1 + row2 + row3

    val insertedRows = rows.transact(xa).unsafePerformSync
    insertedRows
  }

  def findAllPerson(implicit xa: transactor.Transactor[Task]): List[Person] =
    findAll().transact(xa).unsafePerformSync

  def countAll(implicit xa: transactor.Transactor[Task]): Int =
    count().transact(xa).unsafePerformSync

  def deleteAll(implicit xa: transactor.Transactor[Task]): Int =
    delete().run.transact(xa).unsafePerformSync
}

trait PersonRepository {

  def findAll(): ConnectionIO[List[Person]] = {
    sql"select id, name, age from person"
    .query[Person]
    .list
  }

  def count(): ConnectionIO[Int] = {
    sql"select count(name) from person"
      .query[Int]
      .unique
  }

  def delete(): Update0 = {
    sql"delete from person".update
  }

  def persist(name: String, age: Option[Short]): Update0 =
    sql"insert into person (name, age) values ($name, $age)".update

}
