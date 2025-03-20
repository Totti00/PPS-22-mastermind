package mastermind.model.entity

import mastermind.model.entity.HintStone.*
import org.scalatest.flatspec.AnyFlatSpec

class CodeTest extends AnyFlatSpec:

  "Code" should "find the stones in the wrong position" in {
    val inputCode =
      Vector(
        PlayerStoneGrid.fromString("Green"),
        PlayerStoneGrid.fromString("Green"),
        PlayerStoneGrid.fromString("White"),
        PlayerStoneGrid.fromString("White")
      )
    val userInput =
      Vector(
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Green")
      )
    val hintResult = Vector(HintWhite, HintWhite, HintEmpty, HintEmpty)
    val code = Code(inputCode, Vector.empty)
    assert(hintResult == code.compareTo(userInput))

  }

  "Code" should "check the stones in the right position" in {
    val inputCode =
      Vector(
        PlayerStoneGrid.fromString("Green"),
        PlayerStoneGrid.fromString("Green"),
        PlayerStoneGrid.fromString("White"),
        PlayerStoneGrid.fromString("Yellow")
      )
    val userInput =
      Vector(
        PlayerStoneGrid.fromString("Green"),
        PlayerStoneGrid.fromString("Green"),
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Green")
      )
    val hintResult = Vector(HintRed, HintRed, HintEmpty, HintEmpty)
    val code = Code(inputCode, Vector.empty)
    assert(hintResult == code.compareTo(userInput))

  }

  "Code" should "find the stones in the wrong position repeated two times by the user" in {
    val inputCode =
      Vector(
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("White"),
        PlayerStoneGrid.fromString("White")
      )
    val userInput =
      Vector(
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("Yellow")
      )
    val hintResult = Vector(HintWhite, HintWhite, HintEmpty, HintEmpty)
    val code = Code(inputCode, Vector.empty)
    assert(hintResult == code.compareTo(userInput))
  }

  "Code" should "check the stones in the right position using more colors" in {
    val inputCode =
      Vector(
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("White"),
        PlayerStoneGrid.fromString("White")
      )
    val userInput =
      Vector(
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("Blue")
      )
    val hintResult = Vector(HintWhite, HintWhite, HintEmpty, HintEmpty)
    val code = Code(inputCode, Vector.empty)
    assert(hintResult == code.compareTo(userInput))

  }

  "Code" should "check the user input to find the hintVector" in {

    val inputCode =
      Vector(
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("White"),
        PlayerStoneGrid.fromString("Blue")
      )
    val userInput =
      Vector(
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("White"),
        PlayerStoneGrid.fromString("Yellow")
      )
    val hintResult = Vector(HintRed, HintRed, HintWhite, HintWhite)

    val code = Code(inputCode, Vector.empty)
    assert(hintResult == code.compareTo(userInput))

  }
