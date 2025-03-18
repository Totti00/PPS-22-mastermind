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

    def getPlayableStone(row: Int, col: Int): PlayerStoneGrid
    def getHintStone(row: Int, col: Int): HintStone
    def getSizeBoard: (Int, Int)
    def submitGuess(userInput: PlayableStones): HintStones
    def startNewTurn(): Unit
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
            Code(currentMode.codeLength),
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

  trait Interface extends Provider with Component
