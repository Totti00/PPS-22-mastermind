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

  trait Interface extends Provider with Component
