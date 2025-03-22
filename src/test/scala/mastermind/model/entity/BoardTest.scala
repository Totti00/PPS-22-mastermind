package mastermind.model.entity

import mastermind.model.entity.HintStone.{HintEmpty, HintRed, HintWhite}
import mastermind.model.entity.PlayerStoneGrid.Empty
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BoardTest extends AnyFlatSpec with Matchers:

  "Board" should "be created with specified dimensions and default values" in {
    val startBoard = Board(6, 4)
    val firstTestBoard = Board(6, 4, Empty, HintEmpty)
    val secondTestBoard = Board(6, 4, Empty, HintRed)

    startBoard.rows shouldBe 6
    startBoard.cols shouldBe 4

    startBoard shouldBe firstTestBoard
    startBoard should not be secondTestBoard
  }

  "Board" should "give the stone in a specified row correctly" in {
    val board = Board(6, 4, Empty, HintRed)
    board.getPlayableStone(0, 3) shouldBe Empty
    board.getPlayableStone(0, 2) should not be PlayerStoneGrid.fromString("Yellow")
    board.getHintStone(0, 2) shouldBe HintRed
  }

  "placeGuessAndHints" should "update the specified row correctly" in {
    val board = Board(6, 4)
    val newPlayableRow =
      Vector(
        PlayerStoneGrid.fromString("Red"),
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Green"),
        PlayerStoneGrid.fromString("Yellow")
      )
    val newHintRow = Vector(HintRed, HintWhite, HintWhite, HintEmpty)

    val updatedBoard = board.placeGuessAndHints(newPlayableRow, newHintRow, 0)

    updatedBoard.getPlayableStone(0, 0) shouldBe PlayerStoneGrid.fromString("Red")
    updatedBoard.getPlayableStone(0, 1) shouldBe PlayerStoneGrid.fromString("Blue")
    updatedBoard.getPlayableStone(0, 2) shouldBe PlayerStoneGrid.fromString("Green")
    updatedBoard.getPlayableStone(0, 3) shouldBe PlayerStoneGrid.fromString("Yellow")

    updatedBoard.getHintStone(0, 0) shouldBe HintRed
    updatedBoard.getHintStone(0, 1) shouldBe HintWhite
    updatedBoard.getHintStone(0, 2) shouldBe HintWhite
    updatedBoard.getHintStone(0, 3) shouldBe HintEmpty
  }
