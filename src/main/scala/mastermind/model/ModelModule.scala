package mastermind.model

import mastermind.model.GameState.{PlayerLose, PlayerWin}
import mastermind.model.entity.HintStone.HintRed
import mastermind.model.entity.{Board, Code, Game, HintStone, HintStones, PlayableStones, PlayerStoneGrid}
import mastermind.model.strategy.*
import mastermind.utils.ErrorHandler.*

object ModelModule:

  trait Model:
    /** Start a new game
      * @param difficulty
      *   the difficulty of the game
      */
    def startNewGame(difficulty: String): Unit

    /** Reset the game
      */
    def reset(): Unit

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
      */
    def deleteGame(): Unit

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

    /** Colors used to make the code
      * @return
      *   Vector that represent the colors used to make the code
      */
    def colors: PlayableStones

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      private var currentGame: Option[Game] = None
      private var currentMode: GameMode = MediumMode()

      override def startNewGame(difficulty: String): Unit =
        currentMode = giveMeEither {
          difficulty.toLowerCase match
            case "easy"    => EasyMode()
            case "medium"  => MediumMode()
            case "hard"    => HardMode()
            case "extreme" => ExtremeMode()
        } match
          case Right(result) => result
          case Left(_)       => MediumMode()

        currentGame = Some(
          Game(
            Board(currentMode.boardSize._1, currentMode.boardSize._2).initializeCurrentTurn(0),
            Code(currentMode.codeAndColorLength),
            0
          )
        )

      override def reset(): Unit =
        startNewGame(currentMode.name)

      override def deleteGame(): Unit =
        currentGame = None

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
