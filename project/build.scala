import sbt._
import Keys._

object build extends Build {
  val Organisation = "com.tekacs"
  val ScalaVersion = "2.11.5"

  val akkaVersion = "2.3.9"
  val commonSettings = Defaults.coreDefaultSettings ++ Seq(
    sourcesInBase := false,
    organization := Organisation,
    scalaVersion := ScalaVersion,
    resolvers ++= Seq(
      Resolver.sonatypeRepo("public")
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-target:jvm-1.7"
    ),
    libraryDependencies ++= Seq(
      // Testing
      "org.scalacheck" %% "scalacheck" % "1.12.2" % "test",
      "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
      // Config
      "com.typesafe" % "config" % "1.2.1",
      "net.ceedubs" %% "ficus" % "1.1.2",
      "org.json4s" %% "json4s-jackson" % "3.2.11",
      // Networking
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      "net.databinder.dispatch" %% "dispatch-core" % "0.11.2"
    )
  )

  lazy val root = Project(
    "combo-scala",
    file("."),
    settings = commonSettings
  )
  .dependsOn(shared, macros)
  .settings(
    version := "0.1.0",
    libraryDependencies ++= Seq(
    )
  )

  lazy val shared = Project(
    "shared",
    file("./shared/"),
    settings = commonSettings
  )
  .settings(
    name := "shared",
    version := "0.0.1",
    libraryDependencies ++= Seq(
    )
  )

  lazy val macros = Project(
    "macros",
    file("./macros/"),
    settings = commonSettings
  )
  .dependsOn(shared)
  .settings(
    name := "macros",
    version := "0.0.1",
    libraryDependencies ++= Seq(
    )
  )
}
