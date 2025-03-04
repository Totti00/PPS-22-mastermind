package mastermind.model.entity

import org.scalatest.flatspec.AnyFlatSpec

class BoardTest extends AnyFlatSpec:

  // Test di creazione della Board
  "Board" should "be created with specified dimensions and default values" in {
    val startBoard = Board(6, 4)
    val firstTestBoard = Board(6, 4, PlayableStone("Empty"), HintStone("Empty"))
    val secondTestBoard = Board(6, 4, PlayableStone("Empty"), HintStone("Red"))

    assert(startBoard.rows == 6)
    assert(startBoard.cols == 4)

    assert(startBoard == firstTestBoard)
    assert(startBoard != secondTestBoard)
  }

  "Board" should "give the stone in a specified row correctly" in {
    val board = Board(6, 4, PlayableStone("Empty"), HintStone("Red"))
    assert(board.getPlayableStone(0, 3) == PlayableStone("Empty"))
    assert(board.getPlayableStone(0, 2) != PlayableStone("Yellow"))
    assert(board.getHintStone(0, 2) == HintStone("Red"))
  }

  // Test di aggiornamento della board
  "placeGuessAndHints" should "update the specified row correctly" in {
    val board = Board(6, 4)
    val newPlayableRow =
      Vector(PlayableStone("Red"), PlayableStone("Blue"), PlayableStone("Green"), PlayableStone("Yellow"))
    val newHintRow = Vector(HintStone("Red"), HintStone("White"), HintStone("White"), HintStone("Empty"))

    // Aggiorna la prima riga della board
    val updatedBoard = board.placeGuessAndHints(newPlayableRow, newHintRow, 0)

    assert(updatedBoard.getPlayableStone(0, 0) == PlayableStone("Red"))
    assert(updatedBoard.getPlayableStone(0, 1) == PlayableStone("Blue"))
    assert(updatedBoard.getPlayableStone(0, 2) == PlayableStone("Green"))
    assert(updatedBoard.getPlayableStone(0, 3) == PlayableStone("Yellow"))

    assert(updatedBoard.getHintStone(0, 0) == HintStone("Red"))
    assert(updatedBoard.getHintStone(0, 1) == HintStone("White"))
    assert(updatedBoard.getHintStone(0, 2) == HintStone("White"))
    assert(updatedBoard.getHintStone(0, 3) == HintStone("Empty"))
  }
