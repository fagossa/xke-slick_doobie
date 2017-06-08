package com.xebia.slick

import com.xebia.slick.model.{Coffee, Supplier}
import com.xebia.slick.repository.{Coffees, Repository, Suppliers}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object CoffeesAndSuppliersApp extends App{

	implicit val ec = ExecutionContext.global
	val eventualSchemaCreation: Future[Unit] = Repository.resetSchema
			.map(unit => println("Schema created."))
			.recoverWith {
				case err =>
					println(s"Error while creating schema: $err")
					Future.successful(Unit)
			}

	Await.ready(eventualSchemaCreation.flatMap { unit =>
		println("Inserting some data")

		val insertSuppliers = Suppliers.suppliers ++= Seq(
			Supplier(49, "Nescafé", "1 Boulevard Pasteur", "Paris", "France", "75015"),
			Supplier(150, "Maxwell House", "30 bis rue de Paradis", "Paris", "France", "75010"),
			Supplier(101, "Malongo", "1ère avenue, 9ème rue", "Carros", "France", "06513")
		)

		val insertCoffees = Coffees.coffees ++= Seq(
			Coffee("French_Roast", 49, 8.99, 0, 0),
			Coffee("Espresso",    150, 9.99, 0, 0),
			Coffee("Colombian", 101, 7.99, 0, 0)
		)

		val actions = DBIO.seq(insertSuppliers, insertCoffees)

		Repository.db.run(actions)
	}.recover {
		case err => err.printStackTrace()
	}, 10 seconds)
}
