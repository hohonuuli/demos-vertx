import sbt.Package._
import sbt._

lazy val buildSettings = Seq(
  organization := "org.mbari.annosaurus",
  version := "0.1-SNAPSHOT",
  scalaVersion in ThisBuild := Version.Scala,
  crossScalaVersions := Seq(Version.Scala),
  organizationName := "Monterey Bay Aquarium Research Institute",
  startYear := Some(2019),
  licenses += ("Apache-2.0", new URL(
    "https://www.apache.org/licenses/LICENSE-2.0.txt"
  ))
)

lazy val consoleSettings = Seq(
  shellPrompt := { state =>
    val user = System.getProperty("user.name")
    user + "@" + Project.extract(state).currentRef.project + ":sbt> "
  },
  initialCommands in console :=
    """|import io.vertx.lang.scala._
        |import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
        |import io.vertx.scala.core._
        |import scala.concurrent.Future
        |import scala.concurrent.Promise
        |import scala.util.Success
        |import scala.util.Failure
        |val vertx = Vertx.vertx
        |implicit val executionContext = io.vertx.lang.scala.VertxExecutionContext(vertx.getOrCreateContext)
        |""".stripMargin
)

lazy val dependencySettings = Seq(
  resolvers ++= Seq(
    Resolver.mavenLocal,
    Resolver.bintrayRepo("hohonuuli", "maven")
  )
)

lazy val optionSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8", // yes, this is 2 args
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xlint",
    "-Yno-adapted-args",
    "-Xfuture"
  ),
  javacOptions ++= Seq("-target", "11", "-source", "11"),
  updateOptions := updateOptions.value.withCachedResolution(true)
)

lazy val appSettings = buildSettings ++
  consoleSettings ++
  dependencySettings ++
  optionSettings ++ Seq(
  fork := true
)

lazy val `anno-vertx-scala` = (project in file("."))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(appSettings)
  .settings(
    libraryDependencies ++= Seq(
      Library.scalaTest % "test",
      Library.vertx_auth_jwt,
      Library.vertx_codegen,
      Library.vertx_config_hocon,
      Library.vertx_config,
      Library.vertx_lang_scala,
      Library.vertx_web
    ),
    mainClass := Some("io.vertx.core.Launcher"),
    packageOptions += ManifestAttributes(
      ("Main-Verticle", "scala:org.mbari.annosaurs.HttpVerticle")
    )
  )
