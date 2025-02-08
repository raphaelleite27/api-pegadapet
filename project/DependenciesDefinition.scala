import sbt._

object DependenciesDefinition {

  object Versions{
    val akka            = "2.9.3"
    val akkaHttp        = "10.6.3"
    val json4s          = "1.39.2"
    val json4sNative    = "4.0.7"
    val jwtScala        = "9.4.3"
    val doobie          = "1.0.0-RC4"
    val slf4jlog        = "1.7.5"
  }

  val core: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor" % Versions.akka,
    "com.typesafe.akka" %% "akka-actor-typed" % Versions.akka,
    "com.typesafe.akka" %% "akka-stream" % Versions.akka,
    "com.typesafe.akka" %% "akka-http" % Versions.akkaHttp,
    "com.typesafe.akka" %% "akka-http-testkit" % Versions.akkaHttp % Test,
    "org.slf4j" % "slf4j-api" % Versions.slf4jlog,
    "org.slf4j" % "slf4j-simple" % Versions.slf4jlog
  )

  val serialization: Seq[ModuleID] = Seq(
    "de.heikoseeberger" %% "akka-http-json4s" % Versions.json4s,
    "org.json4s" %% "json4s-native" % Versions.json4sNative
  )

  val database: Seq[ModuleID] = Seq(
    "org.tpolecat" %% "doobie-core" % Versions.doobie,
    "org.tpolecat" %% "doobie-postgres" % Versions.doobie,
    "org.tpolecat" %% "doobie-hikari" % Versions.doobie
  )
}
