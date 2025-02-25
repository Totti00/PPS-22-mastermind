package mastermind.view

import mastermind.contoller.ControllerModule
import javafx.fxml.FXMLLoader
import scalafx.scene.Scene
import scalafx.stage.Stage
import javafx.scene.Parent

class GameView(context: ControllerModule.Provider):

  def show(stage: Stage, difficulty: String): Unit =
    val loader = new FXMLLoader(getClass.getResource("/fxml/game.fxml"))
    loader.setController(this)
    val root: Parent = loader.load()

    val namespace = loader.getNamespace

    namespace.get("resetGameButton") match
      case button: javafx.scene.control.Button => button.setOnAction(_ => context.controller.resetGame())
      case _                                   =>

    namespace.get("backButton") match
      case button: javafx.scene.control.Button => button.setOnAction(_ => context.controller.backHome("homepage"))
      case _                                   =>

    context.controller.startGame(difficulty) // Inizializza il gioco con la difficoltà scelta

    import scalafx.Includes.*
    stage.scene = new Scene(root, 800, 500)
    stage.title = s"Mastermind"
    stage.resizable = false
    stage.show()
