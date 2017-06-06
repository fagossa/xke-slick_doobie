package com.xebia.slick.repository

import com.xebia.slick.model.Coffee
import slick.jdbc.PostgresProfile.api._

class Coffees(tag: Tag) extends Table[Coffee](tag, "COFFEES") {
	def name = column[String]("COF_NAME", O.PrimaryKey)
	def supID = column[Int]("SUP_ID")
	def price = column[BigDecimal]("PRICE")
	def sales = column[Int]("SALES", O.Default(0))
	def total = column[Int]("TOTAL", O.Default(0))
	def * = (name, supID, price, sales, total) <> (Coffee.tupled, Coffee.unapply)

	def supplier = foreignKey("SUP_FK", supID, Suppliers.suppliers)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
}

object Coffees {
	val coffees = TableQuery[Coffees]
}
