package mastermind.model

import mastermind.model.entity.{Board, Code, Game}
import mastermind.model.singleton.*

object ModelModule:

  // Factory per generare il gioco in base alla difficoltà
  private object GameFactory:
    def createGame(mode: GameMode): Game =
      Game(Board(mode.boardSize._1, mode.boardSize._2), Code(mode.codeLength), 0)

  trait Model:
    def startNewGame(difficulty: String): Game
    def reset(): Game
    def getPlayableStone(row: Int, col: Int): String
    def getHintStone(row: Int, col: Int): String
    def getSizeBoard: (Int, Int)
    def checkColor(row: Int): Boolean

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
        currentGame = Game(currentGame.board.initializeCurrentTurn(0), currentGame.code, currentGame.currentTurn)
        currentGame

      override def reset(): Game =
        startNewGame(currentDifficulty.name)

      override def getPlayableStone(row: Int, col: Int): String =
        println(row + " " + col + " " + currentGame.board.getPlayableStone(row, col).stringRepresentation)

        currentGame.board.getPlayableStone(row, col).stringRepresentation

      override def getHintStone(row: Int, col: Int): String =
        currentGame.board.getHintStone(row, col).stringRepresentation

      override def getSizeBoard: (Int, Int) = (currentGame.board.rows, currentGame.board.cols)

      override def checkColor(row: Int): Boolean = currentGame.currentTurn.==(row)

  trait Interface extends Provider with Component
