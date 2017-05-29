name := """xke-doobie-example"""

version := "1.0"

scalaVersion := "2.11.7"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core" % "0.4.1", // scalaz + scalaz-stream
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41", // pg-driver
  // Test
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

