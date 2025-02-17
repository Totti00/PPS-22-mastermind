
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "Mastermind",
    scalaVersion := "3.3.5",
    assembly / mainClass := Some("mastermind.main"),
    assembly / assemblyJarName := "mastermind.jar",
    assembly / assemblyMergeStrategy := {
      case "reference.conf"         => MergeStrategy.concat
      case PathList("META-INF", _*) => MergeStrategy.discard
      case _                        => MergeStrategy.first
    },
    libraryDependencies ++= Seq(
      "org.scalafx" %% "scalafx" % "20.0.0-R31"
    ),
    libraryDependencies ++= {
      val platforms = Seq("linux", "mac", "win")
      val modules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")

      for {
        platform <- platforms
        module <- modules
      } yield "org.openjfx" % s"javafx-$module" % "16" classifier platform
    }
  )
