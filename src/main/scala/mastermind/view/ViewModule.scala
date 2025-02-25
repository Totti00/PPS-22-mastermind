package mastermind.view

import javafx.fxml.FXMLLoader
import mastermind.contoller.ControllerModule
import scalafx.scene.Scene
import scalafx.stage.Stage
import javafx.scene.Parent
import javafx.scene.image.Image

object ViewModule:

  trait View:
    def show(stage: Stage): Unit
    def loadView(path: String, mode: Option[String] = None): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>

    private val gameView = new GameView(context)

    class ViewImpl extends View:
      private var stage: Stage = _

      override def show(primaryStage: Stage): Unit =
        stage = primaryStage
        loadView("homepage")

      override def loadView(path: String, mode: Option[String] = None): Unit =
        if path == "game" then gameView.show(stage, mode.getOrElse("medium"))
        else
          val loader = new FXMLLoader(getClass.getResource(s"/fxml/$path.fxml"))
          loader.setController(context.controller)
          val root: Parent = loader.load()

          val namespace = loader.getNamespace
          namespace.get("logo") match
            case logo: javafx.scene.image.ImageView =>
              logo.setImage(new Image(getClass.getResource("/img/mastermind-logo.jpg").toString))
            case _ =>

          import scalafx.Includes.*
          namespace.get("backButton") match
            case button: javafx.scene.control.Button => button.setOnAction(_ => loadView("homepage"))
            case _                                   =>
          namespace.get("rulesButton") match
            case button: javafx.scene.control.Button => button.setOnAction(_ => loadView("rules"))
            case _                                   =>

          val difficultyMapping = Map(
            "easyButton" -> "easy",
            "mediumButton" -> "medium",
            "hardButton" -> "hard",
            "extremeButton" -> "extreme"
          )
          difficultyMapping.foreach { case (buttonId, difficulty) =>
            namespace.get(buttonId) match
              case button: javafx.scene.control.Button => button.setOnAction(_ => loadView("game", Some(difficulty)))
              case _                                   =>
          }

          stage.scene = new Scene(root, 800, 500)
          stage.title = "Mastermind"
          stage.resizable = false
          stage.show()

  trait Interface extends Provider with Component:
    self: Requirements =>
