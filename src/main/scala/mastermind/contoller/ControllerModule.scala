package mastermind.contoller

import mastermind.model.GameState.{PlayerLose, PlayerWin}
import mastermind.model.{GameState, ModelModule}
import mastermind.model.entity.{Game, HintStone, PlayerStoneGrid, Stone}
import mastermind.utils.*
import mastermind.view.ViewModule

object ControllerModule:

  trait Controller:
    /** Reset the game
      */
    def resetGame(difficulty: String): Unit

    def backToMenu(path: String): Unit

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

    /** Gets the current game state.
      *
      * @return
      *   The current `GameState` of the game.
      */
    def gameState: GameState

    /** Sets a new game state.
      *
      * @param newState
      *   The new `GameState` to be set.
      */
    def gameState_(newState: GameState): Unit

  trait Provider:
    val controller: Controller

  type Requirements = ViewModule.Provider with ModelModule.Provider

  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      private var currentGame: Option[Game] = _

      override def resetGame(difficulty: String): Unit =
        currentGame = context.model.reset()
        updateView(Initialize)

      override def backToMenu(path: String): Unit =
        currentGame = context.model.deleteGame()
        goToPage(path)

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
        updateView(UpdateHint, Some(vectorOfHintStones))
        context.model.startNewTurn()
        updateView(UpdatePlayable)

      private def updateView(gameMode: GridUpdateType, vectorOfHintStones: Option[Vector[HintStone]] = None): Unit =
        context.view.updateGameView(gameMode, vectorOfHintStones)

      override def gameState: GameState = context.model.gameState

      override def gameState_(newState: GameState): Unit = context.model.gameState_(newState)

  trait Interface extends Provider with Component:
    self: Requirements =>
