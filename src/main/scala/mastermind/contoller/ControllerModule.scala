package mastermind.contoller

import mastermind.model.{GameState, ModelModule}
import mastermind.model.entity.{Game, HintStone, PlayerStoneGrid, Stone}
import mastermind.view.ViewModule
import scalafx.event.ActionEvent

object ControllerModule:

  trait Controller:
    /** Reset the game
      */
    def resetGame(difficulty: String): Unit

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

    def getStone(row: Int, col: Int, typeStone: String): Stone
    def getSizeBoard: (Int, Int)
    def checkCode(userInput: Vector[PlayerStoneGrid]): Unit

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

    def gameState: GameState

    def gameState_(newState: GameState): Unit

  trait Provider:
    val controller: Controller

  type Requirements = ViewModule.Provider with ModelModule.Provider

  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      private var currentGame: Game = _

      override def resetGame(difficulty: String): Unit =
        currentGame = context.model.reset()
        update()
        // context.view.loadView("game", Option(difficulty))
        // println("Ricomincia il gioco!")

      override def startGame(difficulty: String): Unit =
        currentGame = context.model.startNewGame(difficulty);

      override def goToPage(path: String, mode: Option[String]): Unit = context.view.loadView(path, mode)

      override def getStone(row: Int, col: Int, typeStone: String): Stone =
        typeStone.toLowerCase match
          case "playable" => context.model.getPlayableStone(row, col)
          case "hint"     => context.model.getHintStone(row, col)
          case _          => throw new Exception("wrong request!")

      override def getSizeBoard: (Int, Int) = context.model.getSizeBoard

      override def turn: Int = context.model.currentTurn

      override def remainingTurns: Int = context.model.remainingTurns

      override def checkCode(userInput: Vector[PlayerStoneGrid]): Unit =
        val vectorOfHintStones = context.model.submitGuess(userInput)
        context.model.startNewTurn()
        update(Some(vectorOfHintStones))

      private def update(vectorOfHintStones: Option[Vector[HintStone]] = None): Unit =
        context.view.updateTurns()
        context.view.updateHintGrid(vectorOfHintStones)
        context.view.updatePlayableGrid()

      override def gameState: GameState = context.model.gameState

      override def gameState_(newState: GameState): Unit = context.model.gameState_(newState)

  trait Interface extends Provider with Component:
    self: Requirements =>
