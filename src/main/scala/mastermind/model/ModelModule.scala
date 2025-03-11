package mastermind.model

import mastermind.model.entity.{Board, Code, Game, HintStone, PlayerStoneGrid}
import mastermind.model.singleton.*

object ModelModule:

  // Factory per generare il gioco in base alla difficoltà
  private object GameFactory:
    def createGame(mode: GameMode): Game =
      Game(Board(mode.boardSize._1, mode.boardSize._2).initializeCurrentTurn(0), Code(mode.codeLength), 0)

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

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      private var currentGame: Game = _
      private var currentDifficulty: GameMode = _

      override def startNewGame(difficulty: String): Game =
        println(difficulty)
        val mode = difficulty.toLowerCase match
          case "easy"    => EasyMode
          case "medium"  => MediumMode
          case "hard"    => HardMode
          case "extreme" => ExtremeMode
          case _         => throw new IllegalArgumentException("Invalid difficulty")
        currentDifficulty = mode
        currentGame = GameFactory.createGame(currentDifficulty)
        currentGame

      override def reset(): Game =
        startNewGame(currentDifficulty.name)

      override def getPlayableStone(row: Int, col: Int): PlayerStoneGrid =
        println(row + " " + col + " " + currentGame.board.getPlayableStone(row, col).stringRepresentation)

        currentGame.board.getPlayableStone(row, col)

      override def getHintStone(row: Int, col: Int): HintStone =
        currentGame.board.getHintStone(row, col)

      override def getSizeBoard: (Int, Int) = (currentGame.board.rows, currentGame.board.cols)

      override def currentTurn: Int = currentGame.currentTurn

      override def remainingTurns: Int = currentGame.remainingTurns

      override def submitGuess(userInput: Vector[PlayerStoneGrid]): Vector[HintStone] =
        val vectorOfHintStones = currentGame.code.compareTo(userInput)
        println("modelModule: currentTurn+1: " + currentTurn)
        val newBoard = currentGame.board
          .placeGuessAndHints(userInput, vectorOfHintStones, currentTurn)
        currentGame.board_(newBoard)
        vectorOfHintStones

      override def startNewTurn(): Unit =
        currentGame.currentTurn_()
        val newBoard = currentGame.board.initializeCurrentTurn(currentTurn)
        currentGame.board_(newBoard)

  trait Interface extends Provider with Component
