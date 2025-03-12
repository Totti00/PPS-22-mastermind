package mastermind.view

import javafx.fxml.FXMLLoader
import mastermind.contoller.ControllerModule
import scalafx.scene.Scene
import scalafx.stage.{Popup, Stage}
import javafx.scene.Parent
import mastermind.model.entity.HintStone

object ViewModule:

  trait View:

    /** Displays the view.
      * @param stage
      *   The main window to display the view.
      */
    def show(stage: Stage): Unit

    /** Loads a view from an FXML file.
      * @param path
      *   The path to the FXML file.
      * @param mode
      *   An optional parameter specifying the mode (e.g., difficulty level).
      */
    def loadView(path: String, mode: Option[String] = None): Unit

    /** Update the game view
      * @param hintStones
      *   Optional parameter to update the view with the hint stones
      */
    def updateGameView(hintStones: Option[Vector[HintStone]]): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>

    class ViewImpl extends View:
      private var stage: Stage = _
      private val gameView = new GameView(context)

      override def updateGameView(hintStones: Option[Vector[HintStone]] = None): Unit =
        gameView.updateView(hintStones)

      override def show(primaryStage: Stage): Unit =
        stage = primaryStage
        loadView("MenuPage")

      override def loadView(path: String, mode: Option[String] = None): Unit =
        path match
          case "game" => gameView.show(stage, mode.get)
          case "Rules" =>
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
              case button: javafx.scene.control.Button => button.setOnAction(_ => context.controller.goToPage("Rules"))
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
