package mastermind.model.entity

import mastermind.model.entity.HintStone.{HintEmpty, HintRed, HintWhite}
import mastermind.model.entity.PlayerStone.Empty
import mastermind.model.mode.{EasyMode, MediumMode}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BoardTest extends AnyFlatSpec with Matchers:

  "Board" should "be created with specified dimensions and default values" in {
    val startBoard = Board(6, 4)
    val firstTestBoard = Board(6, 4, Empty, HintEmpty)
    val secondTestBoard = Board(6, 4, Empty, HintRed)

    assert(startBoard.rows == 6)
    assert(startBoard.cols == 4)

    startBoard shouldBe firstTestBoard
    startBoard should not be secondTestBoard
  }

  "Board" should "give the stone in a specified row correctly" in {
    val board = Board(6, 4, Empty, HintRed)
    board.getPlayableStone(0, 3) shouldBe Empty
    board.getPlayableStone(0, 2) should not be PlayerStone.fromString("Yellow")
    board.getHintStone(0, 2) shouldBe HintRed
  }

  "placeGuessAndHints" should "update the specified row correctly" in {
    val board = Board(6, 4)
    val newPlayableRow =
      Vector(
        PlayerStone.fromString("Red"),
        PlayerStone.fromString("Blue"),
        PlayerStone.fromString("Green"),
        PlayerStone.fromString("Yellow")
      )
    val newHintRow = Vector(HintRed, HintWhite, HintWhite, HintEmpty)

    val updatedBoard = board.placeGuessAndHints(newPlayableRow, newHintRow, 0)

    updatedBoard.getPlayableStone(0, 0) shouldBe PlayerStone.fromString("Red")
    updatedBoard.getPlayableStone(0, 1) shouldBe PlayerStone.fromString("Blue")
    updatedBoard.getPlayableStone(0, 2) shouldBe PlayerStone.fromString("Green")
    updatedBoard.getPlayableStone(0, 3) shouldBe PlayerStone.fromString("Yellow")

    updatedBoard.getHintStone(0, 0) shouldBe HintRed
    updatedBoard.getHintStone(0, 1) shouldBe HintWhite
    updatedBoard.getHintStone(0, 2) shouldBe HintWhite
    updatedBoard.getHintStone(0, 3) shouldBe HintEmpty
  }

  "Board" should "manage negative number" in {
    val board = Board(-1, 2)
    assert(board.rows == MediumMode().boardSize._1)
    assert(board.cols == MediumMode().boardSize._2)

    board.rows should not be EasyMode().boardSize._1
    board.cols should not be EasyMode().boardSize._2
  }
