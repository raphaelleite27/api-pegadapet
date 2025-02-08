
import DependenciesDefinition._
import sbt._

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(binSettings)
  .settings(
    name := "api-pegadapet",
    libraryDependencies ++= core ++ serialization ++ database
  )

//resolvers += ("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/").withAllowInsecureProtocol(true)

lazy val binSettings = Seq(
  Compile / mainClass := Some("pegadapet.main.Start"),
  Universal / javaOptions ++= Seq("-Djava.net.preferIPv4Stack=true")
)
