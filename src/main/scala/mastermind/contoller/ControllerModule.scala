package mastermind.contoller

import mastermind.model.ModelModule
import mastermind.model.entity.Game
import mastermind.view.ViewModule
import scalafx.event.ActionEvent

object ControllerModule:

  trait Controller:
    def playGame(event: ActionEvent): Unit
    def exitGame(event: ActionEvent): Unit
    def resetGame(): Unit
    def startGame(difficulty: String): Unit
    def goToPage(path: String, mode: Option[String] = None): Unit

  trait Provider:
    val controller: Controller

  type Requirements = ViewModule.Provider with ModelModule.Provider

  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      private var currentGame: Game = _
      override def playGame(event: ActionEvent): Unit = println("Avvia il gioco!")
      override def exitGame(event: ActionEvent): Unit = println("Esci dal gioco!")
      override def resetGame(): Unit =
        currentGame = context.model.reset()
        println("Ricomincia il gioco!")
      override def startGame(difficulty: String): Unit =
        currentGame = context.model.startNewGame(difficulty);
        println(s"Avvia il gioco con difficoltà $difficulty -> Codice: ${currentGame.code}")

      override def goToPage(path: String, mode: Option[String]): Unit = context.view.loadView(path, mode)

  trait Interface extends Provider with Component:
    self: Requirements =>
