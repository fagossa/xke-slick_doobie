package com.xebia.slick

import com.xebia.slick.repository.Repository
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

object HelloSlick extends  App{

	val query = sql"select 42".as[Int]

	//EC for Future map/flatMap outside db actions that have their own thread pool
	implicit val ec = scala.concurrent.ExecutionContext.global

	//Here db.run() is using Slick's thread pool
	val eventualResults = Repository.db.run(query)

	//Here Await is using our implicit ec
	println(s"Query result: ${Await.result(eventualResults, 2 seconds)}") //Query result: Vector(42)
}
