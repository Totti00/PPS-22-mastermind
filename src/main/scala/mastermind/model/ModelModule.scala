package mastermind.model

import mastermind.model.entity.{Board, Code, Game}

object ModelModule:

  // Factory per generare il gioco in base alla difficoltà
  private object GameFactory:
    def createGame(difficulty: String): Game = difficulty match
      case "easy" => Game(Board(10, 4), new Code(4), 0)
      case "medium" => Game(Board(8, 5), new Code(5), 0)
      case "hard" => Game(Board(6, 5), new Code(5), 0)
      case "extreme" => Game(Board(6, 6), new Code(6), 0)
      case _ => throw new IllegalArgumentException("Invalid difficulty")
      
  trait Model:
    def startNewGame(difficulty: String): Game
    def reset(): Game

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      private var currentGame: Game = _
      private var currentDifficulty: String = _

      override def startNewGame(difficulty: String): Game =
        currentDifficulty = difficulty
        currentGame = GameFactory.createGame(currentDifficulty)
        currentGame
          
      override def reset(): Game =
        startNewGame(currentDifficulty)
  
  trait Interface extends Provider with Component
