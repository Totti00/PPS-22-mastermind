ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "Mastermind",
    scalaVersion := "3.3.5",
    assembly / mainClass := Some("mastermind.Launcher"),
    assembly / assemblyJarName := "mastermind.jar",
    assembly / assemblyMergeStrategy := {
      case "reference.conf"         => MergeStrategy.concat
      case PathList("META-INF", _*) => MergeStrategy.discard
      case _                        => MergeStrategy.first
    },
    libraryDependencies ++= Seq(
      "org.scalafx" %% "scalafx" % "22.0.0-R33",
      "org.scalatest" %% "scalatest" % "3.3.0-SNAP4" % Test,
      "org.scalatestplus" %% "mockito-4-11" % "3.2.18.0" % Test,
      "org.mockito" % "mockito-core" % "5.14.2" % Test
      ////"org.scalamock" %% "scalamock" % "6.0.0" % Test
    ),
    libraryDependencies ++= {
      val platforms = Seq("linux", "mac-aarch64", "win")
      val modules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")

      for {
        platform <- platforms
        module <- modules
      } yield "org.openjfx" % s"javafx-$module" % "16" classifier platform
    }
  )
