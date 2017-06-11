package com.xebia.doobie.query

import com.xebia.doobie.HelloDoobie
import com.xebia.doobie.update.InsertDataExample
import org.scalatest._

import scalaz.Scalaz

class DoobieSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {

  import com.xebia.doobie.common.PgConnection
  implicit val xa = PgConnection.connection

  override protected def beforeAll(configMap: ConfigMap): Unit = {
    info("Creating tables")
    import doobie.imports._
    import Scalaz._

    val drop = sql"DROP TABLE IF EXISTS person".update
    val create =
      sql"""
    CREATE TABLE person (
      id   SERIAL,
      name VARCHAR NOT NULL UNIQUE,
      age  SMALLINT
    )
    """.update
    (drop.run *> create.run).transact(xa).unsafePerformSync
  }

  override protected def afterAll(): Unit = {

  }

  describe("Doobie") {

    describe("connections") {

      it("should do a roundtrip and return a result") {
        HelloDoobie.result should be(42)
      }

    }

    describe("Programs") {

      it("should be used as monads") {
        MonadOrFunctor.resultAsMonad should be(67)
      }

      it("should be used as functors") {
        MonadOrFunctor.resultAsFunctor should be(67)
      }

      it("should insert data into DB") {
        InsertDataExample.persistThreeRecords should be(3)
        InsertDataExample.countAll should be(3)
      }

      it("should fetch data from DB") {
        InsertDataExample.deleteAll
        InsertDataExample.countAll should be(0)
        InsertDataExample.persistThreeRecords should be(3)
        InsertDataExample.findAllPerson
          .map(_.name) should be(List("Alice", "Bob", "John"))
      }

    }
  }

}
