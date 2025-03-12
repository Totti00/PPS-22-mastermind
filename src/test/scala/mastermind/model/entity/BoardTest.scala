package mastermind.model.entity

import mastermind.model.entity.HintStone.{HintEmpty, HintRed, HintWhite}
import mastermind.model.entity.PlayerStoneGrid.Empty
import org.scalatest.flatspec.AnyFlatSpec

class BoardTest extends AnyFlatSpec:

  // Test di creazione della Board
  "Board" should "be created with specified dimensions and default values" in {
    val startBoard = Board(6, 4)
    val firstTestBoard = Board(6, 4, Empty, HintEmpty)
    val secondTestBoard = Board(6, 4, Empty, HintRed)

    assert(startBoard.rows == 6)
    assert(startBoard.cols == 4)

    assert(startBoard == firstTestBoard)
    assert(startBoard != secondTestBoard)
  }

  "Board" should "give the stone in a specified row correctly" in {
    val board = Board(6, 4, Empty, HintRed)
    assert(board.getPlayableStone(0, 3) == Empty)
    assert(board.getPlayableStone(0, 2) != PlayerStoneGrid.fromString("Yellow"))
    assert(board.getHintStone(0, 2) == HintRed)
  }

  // Test di aggiornamento della board
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

    // Aggiorna la prima riga della board
    val updatedBoard = board.placeGuessAndHints(newPlayableRow, newHintRow, 0)

    assert(updatedBoard.getPlayableStone(0, 0) == PlayerStoneGrid.fromString("Red"))
    assert(updatedBoard.getPlayableStone(0, 1) == PlayerStoneGrid.fromString("Blue"))
    assert(updatedBoard.getPlayableStone(0, 2) == PlayerStoneGrid.fromString("Green"))
    assert(updatedBoard.getPlayableStone(0, 3) == PlayerStoneGrid.fromString("Yellow"))

    assert(updatedBoard.getHintStone(0, 0) == HintRed)
    assert(updatedBoard.getHintStone(0, 1) == HintWhite)
    assert(updatedBoard.getHintStone(0, 2) == HintWhite)
    assert(updatedBoard.getHintStone(0, 3) == HintEmpty)
  }
