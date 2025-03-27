package mastermind.model.entity

import mastermind.model.GameState
import mastermind.model.entity.PlayerStone.{Playable, Win}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GameTest extends AnyFlatSpec with Matchers with BeforeAndAfterEach:

  var game: Game = _

  override def beforeEach(): Unit =
    val board = Board(10, 5).initializeCurrentTurn(0)
    val code = Code(5)
    game = Game(board, code, 0)

  "Game" should "initialize correctly with provided board and code" in {
    assert(game.board.rows == 10)
    assert(game.board.cols == 5)
    assert(game.code.colors.length == 5)
    assert(game.remainingTurns == 10)
    assert(game.currentTurn == 0)
    assert(game.state == GameState.InGame)
  }

  "Remaining turns" should "decrease after a turn" in {

    assert(game.remainingTurns == 10)

    game.currentTurn_()

    game.remainingTurns shouldBe 9
  }

  "Game" should "reset correctly" in {

    assert(game.remainingTurns == 10)
    game.currentTurn_()
    assert(game.remainingTurns == 9)

    game = game.resetGame()
    game.remainingTurns shouldBe 10

  }

  "Game state" should "update correctly" in {

    game.state shouldBe GameState.InGame

    game.state_(GameState.PlayerLose)
    game.state shouldBe GameState.PlayerLose

    game.state_(GameState.InGame)
    game.state shouldBe GameState.InGame

    game.state_(GameState.PlayerWin)
    game.state shouldBe GameState.PlayerWin

  }

  "Board" should "be updated correctly" in {

    val newBoard = game.board.winBoard()

    game.board.getPlayableStone(0, 0) shouldBe Playable

    game.board_(newBoard)

    game.board.getPlayableStone(0, 0) shouldBe Win

  }

  "Board" should "not change the actual board if the new board size is different" in {
    assert(game.board.rows == 10)
    assert(game.board.cols == 5)
    game.board_(Board(7, 7).initializeCurrentTurn(0))

    game.board.rows should not be 7
    game.board.cols should not be 7
  }
