lazy val startPGTask = TaskKey[Unit]("startPostgresDocker", "Start postgres in a docker")
startPGTask := {
  import sys.process._
  "docker run -p 5432:5432 --name postgres-db -e POSTGRES_PASSWORD=mysecretpassword -d postgres".!
  println("waiting postgres...")
  Thread.sleep(4000)
  println("postgres ready!")
}

lazy val stopPGTask = TaskKey[Unit]("stopPostgresDocker", "Stop postgres docker")
stopPGTask := {
  import sys.process._
  val res = "docker ps -f name=postgres-db -q".!!
  s"docker rm -f $res".!
  println(s"postgres docker destroyed...")
}

test in Test := ((test in Test) dependsOn startPGTask).value
test in Test := (startPGTask dependsOn stopPGTask).value

fork in run := true
fork in Test := true