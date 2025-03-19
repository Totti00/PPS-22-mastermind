package mastermind.controller

import mastermind.model.GameState.{InGame, PlayerLose, PlayerWin}
import mastermind.model.{GameState, ModelModule}
import mastermind.model.entity.{Game, HintStone, HintStones, PlayableStones, PlayerStoneGrid, Stone}
import mastermind.utils.*
import mastermind.utils.PagesEnum.Menu
import mastermind.view.ViewModule

object ControllerModule:

  trait Controller:
    /** Reset the game
      */
    def resetGame(): Unit

    /** return to the Menu
      */
    def backToMenu(): Unit

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
    def goToPage(path: PagesEnum, mode: Option[String] = None): Unit

    /** Retrieves a stone from the board at a specified position and type
      * @param row
      *   The row index of the stone
      * @param col
      *   The column index of the stone
      * @param typeStone
      *   The type of stone to retrieve ("playable" or "hint")
      * @return
      *   The stone at the specified position.
      */
    def getStone(row: Int, col: Int, typeStone: String): Stone

    /** Retrieves the dimensions (rows and columns) of the game board.
      * @return
      *   A tuple containing the number of rows and columns of the board.
      */
    def getSizeBoard: (Int, Int)

    /** It checks the user's guess and provides feedback on the correctness of the guess.
      * @param userInput
      *   The player's guess as a sequence of `PlayableStones`
      */
    def checkCode(userInput: PlayableStones): Unit

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

    /** Retrieves the list of available colors for the game
      * @return
      *   A sequence of `PlayableStones` representing the available colors.
      */
    def colors: PlayableStones

  trait Provider:
    val controller: Controller

  type Requirements = ViewModule.Provider with ModelModule.Provider

  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      // TODO currentGame puo essere tolto perché non viene mai usato
      private var currentGame: Option[Game] = None

      override def resetGame(): Unit =
        currentGame = context.model.reset()
        updateView(Initialize)

      override def backToMenu(): Unit =
        currentGame = context.model.deleteGame()
        goToPage(Menu)

      override def startGame(difficulty: String): Unit =
        currentGame = context.model.startNewGame(difficulty);

      override def goToPage(path: PagesEnum, mode: Option[String]): Unit = context.view.loadView(path, mode)

      override def getStone(row: Int, col: Int, typeStone: String): Stone =
        typeStone.toLowerCase match
          case "playable" => context.model.getPlayableStone(row, col)
          case "hint"     => context.model.getHintStone(row, col)
          case _          => throw new Exception("wrong request!")

      override def getSizeBoard: (Int, Int) = context.model.getSizeBoard

      override def turn: Int = context.model.currentTurn

      override def remainingTurns: Int = context.model.remainingTurns

      override def checkCode(userInput: PlayableStones): Unit =
        val vectorOfHintStones = context.model.submitGuess(userInput)
        updateView(UpdateHint, Some(vectorOfHintStones))
        if context.model.gameState == InGame then context.model.startNewTurn()
        updateView(UpdatePlayable)

      /** Updates the view with the latest game state, including the feedback and any game updates
        * @param gameMode
        *   The type of update
        * @param vectorOfHintStones
        *   Optional feedback on the user's guess.
        */
      private def updateView(gameMode: GridUpdateType, vectorOfHintStones: Option[HintStones] = None): Unit =
        context.view.updateGameView(gameMode, vectorOfHintStones)

      override def gameState: GameState = context.model.gameState

      override def gameState_(newState: GameState): Unit = context.model.gameState_(newState)

      override def colors: PlayableStones =
        context.model.colors

  trait Interface extends Provider with Component:
    self: Requirements =>
