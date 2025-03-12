package mastermind.model

import mastermind.model.entity.{Board, Code, Game, HintStone, PlayerStoneGrid}
import mastermind.model.strategy.*

object ModelModule:

  trait Model:
    /** Start a new game
      * @param difficulty
      *   the difficulty of the game
      * @return
      *   the new game
      */
    def startNewGame(difficulty: String): Game

    /** Reset the game
      * @return
      *   a new game
      */
    def reset(): Game

    def getPlayableStone(row: Int, col: Int): PlayerStoneGrid
    def getHintStone(row: Int, col: Int): HintStone
    def getSizeBoard: (Int, Int)
    def submitGuess(userInput: Vector[PlayerStoneGrid]): Vector[HintStone]
    def startNewTurn(): Unit

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

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      private var currentGame: Game = _
      private var currentMode: GameMode = MediumMode()

      override def startNewGame(difficulty: String): Game =
        currentMode = difficulty.toLowerCase match
          case "easy"    => EasyMode()
          case "medium"  => MediumMode()
          case "hard"    => HardMode()
          case "extreme" => ExtremeMode()
          case _         => throw new IllegalArgumentException("Invalid difficulty")
        currentGame = Game(
          Board(currentMode.boardSize._1, currentMode.boardSize._2).initializeCurrentTurn(0),
          Code(currentMode.codeLength),
          0
        )
        currentGame

      override def reset(): Game =
        startNewGame(currentMode.name)

      override def getPlayableStone(row: Int, col: Int): PlayerStoneGrid =
        currentGame.board.getPlayableStone(row, col)

      override def getHintStone(row: Int, col: Int): HintStone =
        currentGame.board.getHintStone(row, col)

      override def getSizeBoard: (Int, Int) = (currentGame.board.rows, currentGame.board.cols)

      override def currentTurn: Int = currentGame.currentTurn

      override def remainingTurns: Int = currentGame.remainingTurns

      override def submitGuess(userInput: Vector[PlayerStoneGrid]): Vector[HintStone] =
        val vectorOfHintStones = currentGame.code.compareTo(userInput)
        val newBoard = currentGame.board
          .placeGuessAndHints(userInput, vectorOfHintStones, currentTurn)
        currentGame.board_(newBoard)
        vectorOfHintStones

      override def startNewTurn(): Unit =
        currentGame.currentTurn_()
        if currentTurn < currentGame.board.rows then
          val newBoard = currentGame.board.initializeCurrentTurn(currentTurn)
          currentGame.board_(newBoard)

      override def gameState: GameState = currentGame.state

      override def gameState_(newState: GameState): Unit = currentGame.state_(newState)

  trait Interface extends Provider with Component
