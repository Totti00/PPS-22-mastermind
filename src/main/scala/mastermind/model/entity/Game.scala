package mastermind.model.entity

trait Game:
  var board: Board
  var code: Code
  var currentTurn: Int
  def getCode: Code
  def resetGame(): Game

object Game:
  def apply(field: Board, code: Code, currentTurn: Int): Game = GameImpl(field, code, currentTurn)

  private case class GameImpl(board: Board, code: Code, var currentTurn: Int) extends Game:

    override def getCode: Code = code

    override def resetGame(): Game = Game(Board(board.rows, board.cols), Code(4), 0)
