package com.xebia.slick.repository

import slick.jdbc.PostgresProfile.api._
import Coffees._
import Suppliers._
import slick.jdbc.PostgresProfile

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object Repository {
	val db: PostgresProfile.backend.Database = Database.forConfig("slick-db")

	val schema = coffees.schema ++ suppliers.schema

	def resetSchema: Future[Unit] = {
		/*
			We just want to do:
			db.run(DBIO.seq(schema.drop, schema.create))
			but drop action doesn't include "IF EXISTS" option, so we need some boiler plate to safely re-create the schema
		 */
		implicit val ec = scala.concurrent.ExecutionContext.global
		val triedSession = Try(db.createSession())
		val dropResults = (Try(Seq[String]()) /: schema.dropStatements.map { statement =>

			val triedMesg = triedSession.flatMap(session => Try(session.prepareInsertStatement(statement).executeUpdate())).map(res => s"$statement").recoverWith {
				case err if err.getMessage.contains("does not exist") => Success[String](s"Drop ignored: $err")
				case err => Failure[String](err)
			}

			triedMesg
		}.toSeq) { (trySeqStr, tryStr ) =>
			trySeqStr flatMap (seqStr => tryStr map (str => seqStr :+ str))
		}
		triedSession.flatMap(session => Try(session.close()))

		dropResults match {
			case Failure(err) =>
				Future.failed(err)
			case Success(dropMessages) =>
				//DEBUG: display statements
				println("Initialising DB schema with:")
				dropMessages.foreach(println)
				schema.create.statements.foreach(println)

				db.run(schema.create)
		}
	}
}
