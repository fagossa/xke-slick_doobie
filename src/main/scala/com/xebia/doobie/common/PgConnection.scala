package com.xebia.doobie.common

import doobie.imports.DriverManagerTransactor

import scalaz.concurrent.Task

object PgConnection {

  lazy val connection = DriverManagerTransactor[Task](
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://192.168.99.100/postgres",
    user = "postgres", // default user
    pass = "mysecretpassword"
  )

}
