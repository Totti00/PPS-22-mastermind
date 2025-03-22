package mastermind.model.entity

import mastermind.model.entity.HintStone.{HintEmpty, HintWhite}
import mastermind.model.entity.PlayerStoneGrid.Empty
import org.scalatest.flatspec.AnyFlatSpec

class MatrixTest extends AnyFlatSpec:

  "Matrix" should "be created with specified dimensions and filling value" in {
    val matrix = Matrix(3, 3, PlayerStoneGrid.Empty)

    assert(matrix.rows == 3)
    assert(matrix.cols == 3)
    assert(matrix.cell(0, 0) == Empty)
    assert(matrix.cell(1, 1) == Empty)
    assert(matrix.cell(2, 2) == Empty)
  }

  "Matrix" should "return the correct row" in {
    val matrix = Matrix(3, 3, 0)
    val row = matrix.row(1)

    assert(row == Seq(0, 0, 0))
  }

  "Matrix" should "replace the correct cell value" in {
    val matrix = Matrix(3, 3, 0)
    val updatedMatrix = matrix.replaceCell(1, 1, 5)

    assert(updatedMatrix.cell(1, 1) == 5)
    assert(updatedMatrix.cell(0, 0) == 0)
    assert(updatedMatrix.cell(2, 2) == 0)
  }

  "Matrix" should "replace the correct row" in {
    val matrix = Matrix(3, 3, 0)
    val updatedRow = Vector(1, 1, 1)
    val updatedMatrix = matrix.replaceRow(1, updatedRow)

    assert(updatedMatrix.row(1) == updatedRow)
    assert(updatedMatrix.row(0) == Seq(0, 0, 0))
    assert(updatedMatrix.row(2) == Seq(0, 0, 0))
  }

  "Matrix" should "handle replaceCell on an empty matrix correctly" in {
    assertThrows[Error]:
      val matrix = Matrix(0, 0, 0)
  }

  "Playable Matrix" should "replace the correct cell value" in {
    val matrix: Matrix[PlayerStoneGrid] = Matrix(3, 3, Empty)
    val updatedMatrix = matrix.replaceCell(1, 1, PlayerStoneGrid.fromString("Red"))

    assert(updatedMatrix.cell(1, 1) == PlayerStoneGrid.fromString("Red"))
    assert(updatedMatrix.cell(0, 0) == Empty)
    assert(updatedMatrix.cell(2, 2) == Empty)
  }

  "Hint Matrix" should "replace the correct cell value" in {
    val matrix: Matrix[HintStone] = Matrix(3, 3, HintEmpty)
    val updatedMatrix = matrix.replaceCell(1, 1, HintWhite)

    assert(updatedMatrix.cell(1, 1) == HintWhite)
    assert(updatedMatrix.cell(0, 0) == HintEmpty)
    assert(updatedMatrix.cell(2, 2) != Empty)

  }
