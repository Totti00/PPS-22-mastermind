package mastermind.model.entity

import scalafx.animation.{Animation, KeyFrame, Timeline}
import scalafx.util.Duration

import java.text.SimpleDateFormat

trait Game:
  /** Board of the game
    * @return
    *   the board of the game
    */
  def board: Board
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
  def currentTurn: Int // Getter

  /** Set the current turn
    */
  def currentTurn_(): Unit // Setter senza parametri

object Game:
  def apply(field: Board, code: Code, currentTurn: Int): Game = GameImpl(field, code, currentTurn)

  private case class GameImpl(var board: Board, override val code: Code, private var _currentTurn: Int) extends Game:

    private var startTime = System.currentTimeMillis()
    private val timeFormat = new SimpleDateFormat("HH:mm:ss")

    override def resetGame(): Game = Game(Board(board.rows, board.cols), Code(4), 0)

    override def remainingTurns: Int = board.rows - currentTurn

    // Getter
    override def currentTurn: Int = _currentTurn

    // Setter senza parametri (incrementa il turno)
    override def currentTurn_(): Unit = _currentTurn += 1

    override def board_(newBoard: Board): Unit = this.board = newBoard
