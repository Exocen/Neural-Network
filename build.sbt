// Assembly will create a "fat jar"
import AssemblyKeys._

assemblySettings

jarName in assembly   := "nesi.jar"

mainClass in assembly := Some("fr.cristal.emeraude.n2s3.Main")

// Details & options for this project
organization          := "fr.cristal.emeraude"

name                  := "n2s3"

scalaVersion          := "2.11.4"

scalacOptions         := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

// Dependencies
libraryDependencies ++= {
  val akkaV = "2.3.7"
  val scalatestV = "2.2.1"
  Seq(
    "com.typesafe.akka" %% "akka-actor"      % akkaV,
    "com.typesafe.akka" %% "akka-testkit"    % akkaV,
    "org.scalatest"     %% "scalatest"       % scalatestV % "test"
  )
}
