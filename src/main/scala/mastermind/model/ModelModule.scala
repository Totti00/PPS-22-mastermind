package mastermind.model

import mastermind.model.GameState.{InGame, PlayerLose, PlayerWin}
import mastermind.model.entity.HintStone.HintRed
import mastermind.model.entity.{Board, Code, Game, HintStone, PlayerStoneGrid, HintStones, PlayableStones}
import mastermind.model.strategy.*

object ModelModule:

  trait Model:
    /** Start a new game
      * @param difficulty
      *   the difficulty of the game
      * @return
      *   the new game
      */
    def startNewGame(difficulty: String): Option[Game]

    /** Reset the game
      * @return
      *   a new game
      */
    def reset(): Option[Game]

    /** Retrieves a specific playable stone at the given row and column.
      * @param row
      *   The row of the stone in the board.
      * @param col
      *   The column of the stone in the board.
      * @return
      *   The `PlayerStoneGrid` at the given position
      */
    def getPlayableStone(row: Int, col: Int): PlayerStoneGrid

    /** Retrieves a specific hint stone at the given row and column.
      * @param row
      *   The row of the hint stone in the board
      * @param col
      *   The column of the hint stone in the board
      * @return
      *   The `HintStone` at the given position
      */
    def getHintStone(row: Int, col: Int): HintStone

    /** Retrieves the size of the board
      * @return
      *   A tuple `(rows, cols)` representing the dimensions of the board
      */
    def getSizeBoard: (Int, Int)

    /** Submits a guess from the player and receives the corresponding feedback of hint stones
      * @param userInput
      *   The player's guess
      * @return
      *   A sequence of `HintStones` representing feedback for the guess
      */
    def submitGuess(userInput: PlayableStones): HintStones

    /** Starts a new turn in the game
      */
    def startNewTurn(): Unit

    /** Deletes the current game instance
      * @return
      *   An `Option` representing the deleted game
      */
    def deleteGame(): Option[Game]

    /** Current turn
      * @return
      *   the current turn
      */
    def currentTurn: Int

    /** Remaining turns
      * @return
      *   the remaining turns
      */
    def remainingTurns: Int

    /** Game state getter
      * @return
      *   the current game state
      */
    def gameState: GameState

    /** Game state setter
      * @param newState
      *   the new game state
      */
    def gameState_(newState: GameState): Unit

    def colors: PlayableStones

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      private var currentGame: Option[Game] = None
      private var currentMode: GameMode = MediumMode()

      override def startNewGame(difficulty: String): Option[Game] =
        currentMode = difficulty.toLowerCase match
          case "easy"    => EasyMode()
          case "medium"  => MediumMode()
          case "hard"    => HardMode()
          case "extreme" => ExtremeMode()
          case _         => throw new IllegalArgumentException("Invalid difficulty")
        currentGame = Some(
          Game(
            Board(currentMode.boardSize._1, currentMode.boardSize._2).initializeCurrentTurn(0),
            Code(currentMode.codeAndColorLength),
            0
          )
        )
        currentGame

      override def reset(): Option[Game] =
        startNewGame(currentMode.name)

      override def deleteGame(): Option[Game] =
        currentGame = None
        currentGame

      override def getPlayableStone(row: Int, col: Int): PlayerStoneGrid =
        currentGame.get.board.getPlayableStone(row, col)

      override def getHintStone(row: Int, col: Int): HintStone =
        currentGame.get.board.getHintStone(row, col)

      override def getSizeBoard: (Int, Int) = (currentGame.get.board.rows, currentGame.get.board.cols)

      override def currentTurn: Int = currentGame.get.currentTurn

      override def remainingTurns: Int = currentGame.get.remainingTurns

      override def submitGuess(userInput: PlayableStones): HintStones =
        val vectorOfHintStones = currentGame.get.code.compareTo(userInput)
        val newBoard = currentGame.get.board
          .placeGuessAndHints(userInput, vectorOfHintStones, currentTurn)
        currentGame.get.board_(newBoard)
        if checkWin(vectorOfHintStones) then
          gameState_(PlayerWin)
          currentGame.get.board_(currentGame.get.board.winBoard())
        vectorOfHintStones

      private def checkWin(hintStonesFeedback: HintStones): Boolean =
        hintStonesFeedback.forall(_ == HintRed)

      override def startNewTurn(): Unit =
        currentGame.get.currentTurn_()
        if currentGame.get.remainingTurns == 0 then gameState_(PlayerLose)
        if currentTurn < currentGame.get.board.rows then
          val newBoard = currentGame.get.board.initializeCurrentTurn(currentTurn)
          currentGame.get.board_(newBoard)

      override def gameState: GameState = currentGame.get.state

      override def gameState_(newState: GameState): Unit = currentGame.get.state_(newState)

      override def colors: PlayableStones =
        currentGame.get.code.colors

  trait Interface extends Provider with Component
