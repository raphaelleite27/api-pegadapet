import sbt._

object DependenciesDefinition {

  object Versions{
    val akka            = "2.9.3"
    val akkaHttp        = "10.6.3"
    val json4s          = "1.39.2"
    val json4sNative    = "4.0.7"
    val jwtScala        = "9.4.3"
    val doobie          = "1.0.0-RC4"
    val slf4jlog        = "2.8.0"
  }

  val core: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor" % Versions.akka,
    "com.typesafe.akka" %% "akka-actor-typed" % Versions.akka,
    "com.typesafe.akka" %% "akka-stream" % Versions.akka,
    "com.typesafe.akka" %% "akka-http" % Versions.akkaHttp,
    "com.typesafe.akka" %% "akka-http-testkit" % Versions.akkaHttp % Test,
    "com.typesafe.akka" %% "akka-slf4j" % Versions.slf4jlog,
    "ch.qos.logback" % "logback-classic" % "1.4.5",
    "net.logstash.logback" % "logstash-logback-encoder" % "7.3"
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
