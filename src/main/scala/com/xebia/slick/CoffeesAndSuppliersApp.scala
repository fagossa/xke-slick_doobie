package com.xebia.slick

import com.xebia.slick.repository.Repository
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object CoffeesAndSuppliersApp extends App{

	implicit val ec = ExecutionContext.global
	val eventualSchemaCreation = Repository.resetSchema
			.map(unit => println("Schema created."))
			.recoverWith {
				case err =>
					println(s"Error while creating schema: $err")
					//err.printStackTrace()
					Future.successful(Unit)
			}

	Await.ready(eventualSchemaCreation, 10 seconds)
}
