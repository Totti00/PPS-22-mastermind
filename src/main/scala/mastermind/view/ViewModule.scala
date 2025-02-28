package mastermind.view

import javafx.fxml.FXMLLoader
import mastermind.contoller.ControllerModule
import scalafx.scene.Scene
import scalafx.stage.{Popup, Stage}
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
        loadView("MenuPage")

      override def loadView(path: String, mode: Option[String] = None): Unit =
        println("loadView")

        path match
          case "game" => gameView.show(stage, mode.get)
          case "rule" =>
            println("rule")
            val loader = new FXMLLoader(getClass.getResource(s"/fxml/$path.fxml"))
            val root: Parent = loader.load()
            val popup = new Popup()
            popup.getContent.clear()
            popup.getContent.add(root)
            popup.show(stage)
            val namespace = loader.getNamespace

            import scalafx.Includes.*

            namespace.get("ruleExitButton") match
              case button: javafx.scene.control.Button => button.setOnAction(_ => popup.hide())
              case _                                   =>

          case _ =>
            val loader = new FXMLLoader(getClass.getResource(s"/fxml/$path.fxml"))
            loader.setController(context.controller)
            val root: Parent = loader.load()
            val namespace = loader.getNamespace

            import scalafx.Includes.*
            namespace.get("rulesButton") match
              case button: javafx.scene.control.Button => button.setOnAction(_ => context.controller.goToPage("rule"))
              case _                                   =>

            val difficultyMapping = Map(
              "easyModeButton" -> "easy",
              "mediumModeButton" -> "medium",
              "hardModeButton" -> "hard",
              "extremeModeButton" -> "extreme"
            )
            difficultyMapping.foreach { case (buttonId, difficulty) =>
              namespace.get(buttonId) match
                case button: javafx.scene.control.Button =>
                  button.setOnAction(_ => context.controller.goToPage("game", Some(difficulty)))
                case _ =>
            }
            stage.scene = new Scene(root, 800, 500)
            stage.title = "Mastermind"
            stage.show()

  trait Interface extends Provider with Component:
    self: Requirements =>
