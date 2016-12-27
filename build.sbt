name := "old-good-prime-numbers"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.storm-enroute" %% "scalameter-core" % "0.6",
  "com.github.scala-blitz" %% "scala-blitz" % "1.1",
  "org.scala-lang.modules" %% "scala-swing" % "1.0.1",
  "com.storm-enroute" %% "scalameter" % "0.6" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.1" % "test",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.apache.spark" %% "spark-core" % "1.2.1"
)

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

logBuffered in Test := false
