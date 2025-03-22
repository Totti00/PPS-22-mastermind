package mastermind.model.entity

import mastermind.model.GameState
import mastermind.model.entity.PlayerStoneGrid.{Playable, Win}
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
    game.board.rows shouldBe 10
    game.board.cols shouldBe 5
    game.code.colors.length shouldBe 5
    game.remainingTurns shouldBe 10
    game.currentTurn shouldBe 0
    game.state shouldBe GameState.InGame
  }

  "Remaining turns" should "decrease after a turn" in {

    game.remainingTurns shouldBe 10

    game.currentTurn_()

    game.remainingTurns shouldBe 9
  }

  "Game" should "reset correctly" in {

    game.remainingTurns shouldBe 10
    game.currentTurn_()
    game.remainingTurns shouldBe 9

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
