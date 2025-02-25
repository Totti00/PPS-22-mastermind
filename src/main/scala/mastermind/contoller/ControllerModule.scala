package mastermind.contoller

import mastermind.model.ModelModule
import mastermind.view.ViewModule
import scalafx.event.ActionEvent

object ControllerModule:

  trait Controller:
    def playGame(event: ActionEvent): Unit
    def exitGame(event: ActionEvent): Unit
    def resetGame(): Unit
    def startGame(difficulty: String): Unit
    def backHome(path: String): Unit

  trait Provider:
    val controller: Controller

  type Requirements = ViewModule.Provider with ModelModule.Provider

  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      override def playGame(event: ActionEvent): Unit = println("Avvia il gioco!")
      override def exitGame(event: ActionEvent): Unit = println("Esci dal gioco!")
      override def resetGame(): Unit = println("Ricomincia il gioco!")
      override def startGame(difficulty: String): Unit = println(s"Avvia il gioco con difficoltà $difficulty")

      override def backHome(path: String): Unit = context.view.loadView(path)

  trait Interface extends Provider with Component:
    self: Requirements =>
