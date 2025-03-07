package mastermind.model.entity

trait Game:
  def board: Board
  def code: Code
  def currentTurn: Int
  def resetGame(): Game

object Game:
  def apply(field: Board, code: Code, currentTurn: Int): Game = GameImpl(field, code, currentTurn)

  private case class GameImpl(override val board: Board, override val code: Code, override val currentTurn: Int)
      extends Game:

    override def resetGame(): Game = Game(Board(board.rows, board.cols), Code(4), 0)
