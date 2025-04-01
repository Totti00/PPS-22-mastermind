package mastermind.view

import javafx.fxml.FXMLLoader
import mastermind.controller.ControllerModule
import scalafx.stage.Stage
import mastermind.model.entity.HintStones
import mastermind.utils.GridUpdateType
import mastermind.utils.PagesEnum.*
import mastermind.utils.PagesEnum

object ViewModule:

  trait View:

    /** Displays the view.
      * @param stage
      *   The main window to display the view.
      */
    def initialize(stage: Stage): Unit

    /** Loads a view from an FXML file.
      * @param path
      *   The path to the FXML file.
      * @param mode
      *   An optional parameter specifying the mode (e.g., difficulty level).
      */
    def loadView(path: PagesEnum, mode: Option[String] = None): Unit

    /** Updates the game view based on the specified game mode
      * @param gameMode
      *   The type of update to be applied to the game grid
      * @param hintStones
      *   Optional parameter to update the view with the hint stones
      */
    def updateGameView(gameMode: GridUpdateType, hintStones: Option[HintStones]): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>

    class ViewImpl extends View:
      private var stage: Option[Stage] = None
      private var gameView: Option[GameView] = None

      override def updateGameView(gameMode: GridUpdateType, hintStones: Option[HintStones] = None): Unit =
        gameView.get.updateView(gameMode, hintStones)

      override def initialize(primaryStage: Stage): Unit =
        stage = Some(primaryStage)
        loadView(Menu)

      override def loadView(path: PagesEnum, mode: Option[String] = None): Unit =
        val load = loadFXML(path)
        path match
          case Game =>
            context.controller.startGame(mode.get)
            gameView = Some(GameView(context.controller, stage.get))
            load(gameView.get)
          case Rules => load(RulesView(stage.get))
          case Menu  => load(MenuView(context.controller, stage.get))

      private def loadFXML[T](path: PagesEnum)(controller: T): Unit =
        val loader = new FXMLLoader(getClass.getResource(s"/fxml/$path.fxml"))
        loader.setController(controller)
        loader.load()

  trait Interface extends Provider with Component:
    self: Requirements =>
