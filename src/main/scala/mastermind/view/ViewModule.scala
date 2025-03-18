package mastermind.view

import javafx.fxml.FXMLLoader
import mastermind.contoller.ControllerModule
import scalafx.scene.Scene
import scalafx.stage.Stage
import javafx.scene.Parent
import mastermind.model.entity.HintStone
import mastermind.utils.GridUpdateType

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

    /** Updates the game view based on the specified game mode
      * @param gameMode
      *   The type of update to be applied to the game grid
      * @param hintStones
      *   Optional parameter to update the view with the hint stones
      */
    def updateGameView(gameMode: GridUpdateType, hintStones: Option[Vector[HintStone]]): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>

    class ViewImpl extends View:
      private var stage: Stage = _
      private var gameView2: GameView2 = _

      override def updateGameView(gameMode: GridUpdateType, hintStones: Option[Vector[HintStone]] = None): Unit =
        gameView2.updateGrids(gameMode, hintStones)

      override def show(primaryStage: Stage): Unit =
        stage = primaryStage
        loadView("MenuPage")

      override def loadView(path: String, mode: Option[String] = None): Unit =
        path match
          case "Game" =>
            context.controller.startGame(mode.get)
            val loader = new FXMLLoader(getClass.getResource(s"/fxml/$path.fxml"))
            val gameView = GameView2(context.controller, stage)
            loader.setController(gameView)
            val root: Parent = loader.load()
            this.gameView2 = gameView

          case "Rules" =>
            val loader = new FXMLLoader(getClass.getResource(s"/fxml/$path.fxml"))
            import scalafx.Includes.*
            val rulesView = RulesView(stage)
            loader.setController(rulesView)
            val root: Parent = loader.load()

          case _ =>
            val loader = new FXMLLoader(getClass.getResource(s"/fxml/$path.fxml"))
            loader.setController(MenuView(context.controller))
            val root: Parent = loader.load()
            import scalafx.Includes.*
            stage.scene = new Scene(root, 800, 500)
            stage.title = "Mastermind"
            stage.show()

  trait Interface extends Provider with Component:
    self: Requirements =>
