package com.xebia.slick

import com.xebia.slick.model.{Coffee, Supplier}
import com.xebia.slick.repository.Coffees._
import com.xebia.slick.repository.Suppliers._
import com.xebia.slick.repository.{Coffees, Repository, Suppliers}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object CoffeesAndSuppliersApp extends App{

	implicit val ec = ExecutionContext.global
	val eventualSchemaCreation: Future[Unit] = Repository.resetSchema
			.map(unit => println("✨ Schema created. ✨"))
			.recoverWith {
				case err =>
					println(s"💥 Error while creating schema: $err")
					Future.successful(Unit)
			}

	Await.ready(eventualSchemaCreation.flatMap { unit =>
		println("⚡️ Inserting some data")

		val insertSuppliers = suppliers ++= Seq(
			Supplier(49, "Nescafé", "1 Boulevard Pasteur", "Paris", "France", "75015"),
			Supplier(150, "Maxwell House", "30 bis rue de Paradis", "Paris", "France", "75010"),
			Supplier(101, "Malongo", "1ère avenue, 9ème rue", "Carros", "France", "06513")
		)

		val insertCoffees = coffees ++= Seq(
			Coffee("French_Roast", 49, 8.99, 0, 0),
			Coffee("Espresso",    150, 9.99, 0, 0),
			Coffee("Colombian", 101, 7.99, 0, 0)
		)

		val actions = DBIO.seq(insertSuppliers, insertCoffees)

		Repository.db.run(actions)
	}.flatMap{ unit =>
		println("\uD83D\uDD0D See inserted coffees")
		Repository.db.run(coffees.result).map { foundCoffees =>
			foundCoffees.foreach(println)
		}
	}.flatMap{ unit =>
		println("⚡️ Updating Expresso price")
		val query = for {
			c <- coffees if c.name === "Espresso"
		} yield c.price
		val action = query.update(10.49)
		Repository.db.run(action)
	}.flatMap{ unit =>
		println("\uD83D\uDD0D See modified coffee price")
		Repository.db.run(coffees.result).map { foundCoffees =>
			foundCoffees.foreach(println)
		}
	}.flatMap{ unit =>
		println("\uD83D\uDD0D cross join")
		/* //without for:
		val crossJoin = (coffees join suppliers).map { (joined: (Coffees, Suppliers)) =>
			(joined._1.name, joined._2.name)
		}*/
		//with for
		val crossJoin = for {
			(c, s) <- coffees join suppliers
		} yield (c.name, s.name)


		Repository.db.run(crossJoin.result).map { found =>
			found.foreach(println)
		}
	}.flatMap{ unit =>
		println("\uD83D\uDD0D inner join")
		val innerJoin = for {
			(c, s) <- coffees join suppliers on (_.supID === _.id)
		} yield (c.name, s.name)

		Repository.db.run(innerJoin.result).map { foundCoffees =>
			foundCoffees.foreach(println)
		}
	}.recover {
		case err => err.printStackTrace()
	}, 10 seconds)
}
