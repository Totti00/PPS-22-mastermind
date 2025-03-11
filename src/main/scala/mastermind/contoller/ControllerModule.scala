package mastermind.contoller

import mastermind.model.ModelModule
import mastermind.model.entity.Game
import mastermind.view.ViewModule
import scalafx.event.ActionEvent

object ControllerModule:

  trait Controller:
    /** Reset the game
      */
    def resetGame(): Unit

    /** Start a new game
      * @param difficulty
      *   the difficulty of the game
      */
    def startGame(difficulty: String): Unit

    /** Go to a specific page
      * @param path
      *   the path of the page
      * @param mode
      *   the difficulty of the game
      */
    def goToPage(path: String, mode: Option[String] = None): Unit

    def getStone(row: Int, col: Int, typeStone: String): String
    def getSizeBoard: (Int, Int)

    /** Current turn
      * @return
      *   the current turn
      */
    def turn: Int

    /** Remaining turns
      * @return
      *   the remaining turns
      */
    def remainingTurns: Int
  trait Provider:
    val controller: Controller

  type Requirements = ViewModule.Provider with ModelModule.Provider

  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      private var currentGame: Game = _
      override def resetGame(): Unit =
        currentGame = context.model.reset()
        println("Ricomincia il gioco!")
      override def startGame(difficulty: String): Unit =
        currentGame = context.model.startNewGame(difficulty);
        println(s"Avvia il gioco con difficoltà $difficulty -> Codice: ${currentGame.code}")

      override def goToPage(path: String, mode: Option[String]): Unit = context.view.loadView(path, mode)

      override def getStone(row: Int, col: Int, typeStone: String): String =
        typeStone.toLowerCase match
          case "playable" => context.model.getPlayableStone(row, col)
          case "hint"     => context.model.getHintStone(row, col)
          case _          => ???

      override def getSizeBoard: (Int, Int) = context.model.getSizeBoard

      override def turn: Int = context.model.currentTurn

      override def remainingTurns: Int = context.model.remainingTurns

  trait Interface extends Provider with Component:
    self: Requirements =>
