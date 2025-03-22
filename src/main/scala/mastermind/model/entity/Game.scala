package mastermind.model.entity

import mastermind.model.GameState
import java.text.SimpleDateFormat

trait Game:
  /** Board of the game
    * @return
    *   the board of the game
    */
  def board: Board

  /** Set the board with the newBoard
    * @param newBoard
    *   the board to use
    */
  def board_(newBoard: Board): Unit

  /** Code to guess
    * @return
    *   the code to guess
    */
  def code: Code

  /** Reset the game
    * @return
    *   a new game
    */
  def resetGame(): Game

  /** Remaining turns
    * @return
    *   the remaining turns
    */
  def remainingTurns: Int

  /** Current turn
    * @return
    *   the current turn
    */
  def currentTurn: Int

  /** Set the current turn
    */
  def currentTurn_(): Unit

  /** Game state getter
    * @return
    *   the current game state
    */
  def state: GameState

  /** Game state setter
    * @param newState
    *   the new game state
    */
  def state_(newState: GameState): Unit

object Game:
  def apply(field: Board, code: Code, currentTurn: Int): Game = GameImpl(field, code, currentTurn, GameState.InGame)

  private case class GameImpl(
      var board: Board,
      override val code: Code,
      private var _currentTurn: Int,
      private var _state: GameState
  ) extends Game:

    override def resetGame(): Game = Game(Board(board.rows, board.cols), Code(4), 0)

    override def remainingTurns: Int = board.rows - currentTurn

    override def currentTurn: Int = _currentTurn

    override def currentTurn_(): Unit = _currentTurn += 1

    override def board_(newBoard: Board): Unit = newBoard match
      case board if board.rows == this.board.rows && board.cols == this.board.cols => this.board = newBoard
      case _ => throw Exception("new board size is different")

    override def state: GameState = _state

    override def state_(newState: GameState): Unit = _state = newState
