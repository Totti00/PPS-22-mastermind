package mastermind.model.entity

import mastermind.model.entity.HintStone.{HintEmpty, HintWhite}
import mastermind.model.entity.PlayerStoneGrid.Empty
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MatrixTest extends AnyFlatSpec with Matchers:

  "Matrix" should "be created with specified dimensions and filling value" in {
    val matrix = Matrix(3, 3, PlayerStoneGrid.Empty)

    assert(matrix.rows == 3)
    assert(matrix.cols == 3)
    matrix.cell(0, 0) shouldBe Empty
    matrix.cell(1, 1) shouldBe Empty
    matrix.cell(2, 2) shouldBe Empty
  }

  "Matrix" should "return the correct row" in {
    val matrix = Matrix(3, 3, 0)
    val row = matrix.row(1)

    row shouldBe Seq(0, 0, 0)
  }

  "Matrix" should "replace the correct cell value" in {
    val matrix = Matrix(3, 3, 0)
    val updatedMatrix = matrix.replaceCell(1, 1, 5)

    assert(updatedMatrix.cell(1, 1) == 5)
    assert(updatedMatrix.cell(0, 0) == 0)
    updatedMatrix.cell(2, 2) shouldBe 0
  }

  "Matrix" should "replace the correct row" in {
    val matrix = Matrix(3, 3, 0)
    val updatedRow = Vector(1, 1, 1)
    val updatedMatrix = matrix.replaceRow(1, updatedRow)

    assert(updatedMatrix.row(1) == updatedRow)
    assert(updatedMatrix.row(0) == Seq(0, 0, 0))
    updatedMatrix.row(2) shouldBe Seq(0, 0, 0)
  }

  "Playable Matrix" should "replace the correct cell value" in {
    val matrix: Matrix[PlayerStoneGrid] = Matrix(3, 3, Empty)
    val updatedMatrix = matrix.replaceCell(1, 1, PlayerStoneGrid.fromString("Red"))

    updatedMatrix.cell(1, 1) shouldBe PlayerStoneGrid.fromString("Red")
    updatedMatrix.cell(0, 0) shouldBe Empty
    updatedMatrix.cell(2, 2) shouldBe Empty
  }

  "Hint Matrix" should "replace the correct cell value" in {
    val matrix: Matrix[HintStone] = Matrix(3, 3, HintEmpty)
    val updatedMatrix = matrix.replaceCell(1, 1, HintWhite)

    updatedMatrix.cell(1, 1) shouldBe HintWhite
    updatedMatrix.cell(0, 0) shouldBe HintEmpty
    updatedMatrix.cell(2, 2) should not be Empty

  }
