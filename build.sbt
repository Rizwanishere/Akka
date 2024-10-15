ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val scalaTestVersion = "3.2.14"

lazy val root = (project in file("."))
  .settings(
    name := "Akka",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.6.20",
      "com.typesafe.akka" %% "akka-stream" % "2.6.20",
      "com.typesafe.akka" %% "akka-testkit" % "2.6.20",
      "org.scalatest" %% "scalatest" % scalaTestVersion,

    )
  )

