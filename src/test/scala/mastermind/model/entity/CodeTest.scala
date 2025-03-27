package mastermind.model.entity

import mastermind.model.entity.HintStone.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CodeTest extends AnyFlatSpec with Matchers:

  "Code" should "find the stones in the wrong position" in {
    val inputCode =
      Vector(
        PlayerStone.fromString("Green"),
        PlayerStone.fromString("Green"),
        PlayerStone.fromString("White"),
        PlayerStone.fromString("White")
      )
    val userInput =
      Vector(
        PlayerStone.fromString("Blue"),
        PlayerStone.fromString("Blue"),
        PlayerStone.fromString("Blue"),
        PlayerStone.fromString("Green")
      )
    val hintResult = Vector(HintWhite, HintWhite, HintEmpty, HintEmpty)
    val code = Code(inputCode, Vector.empty)
    code.compareTo(userInput) shouldBe hintResult

  }

  "Code" should "check the stones in the right position" in {
    val inputCode =
      Vector(
        PlayerStone.fromString("Green"),
        PlayerStone.fromString("Green"),
        PlayerStone.fromString("White"),
        PlayerStone.fromString("Yellow")
      )
    val userInput =
      Vector(
        PlayerStone.fromString("Green"),
        PlayerStone.fromString("Green"),
        PlayerStone.fromString("Blue"),
        PlayerStone.fromString("Green")
      )
    val hintResult = Vector(HintRed, HintRed, HintEmpty, HintEmpty)
    val code = Code(inputCode, Vector.empty)
    code.compareTo(userInput) shouldBe hintResult

  }

  "Code" should "find the stones in the wrong position repeated two times by the user" in {
    val inputCode =
      Vector(
        PlayerStone.fromString("Yellow"),
        PlayerStone.fromString("Yellow"),
        PlayerStone.fromString("White"),
        PlayerStone.fromString("White")
      )
    val userInput =
      Vector(
        PlayerStone.fromString("Blue"),
        PlayerStone.fromString("Blue"),
        PlayerStone.fromString("Yellow"),
        PlayerStone.fromString("Yellow")
      )
    val hintResult = Vector(HintWhite, HintWhite, HintEmpty, HintEmpty)
    val code = Code(inputCode, Vector.empty)
    code.compareTo(userInput) shouldBe hintResult
  }

  "Code" should "check the stones in the right position using more colors" in {
    val inputCode =
      Vector(
        PlayerStone.fromString("Yellow"),
        PlayerStone.fromString("Yellow"),
        PlayerStone.fromString("White"),
        PlayerStone.fromString("White")
      )
    val userInput =
      Vector(
        PlayerStone.fromString("Blue"),
        PlayerStone.fromString("Blue"),
        PlayerStone.fromString("Yellow"),
        PlayerStone.fromString("Blue")
      )
    val hintResult = Vector(HintWhite, HintWhite, HintEmpty, HintEmpty)
    val code = Code(inputCode, Vector.empty)
    assert(hintResult == code.compareTo(userInput))
    code.compareTo(userInput) shouldBe hintResult

  }

  "Code" should "check the user input to find the hintVector" in {

    val inputCode =
      Vector(
        PlayerStone.fromString("Yellow"),
        PlayerStone.fromString("Yellow"),
        PlayerStone.fromString("White"),
        PlayerStone.fromString("Blue")
      )
    val userInput =
      Vector(
        PlayerStone.fromString("Yellow"),
        PlayerStone.fromString("Blue"),
        PlayerStone.fromString("White"),
        PlayerStone.fromString("Yellow")
      )
    val hintResult = Vector(HintRed, HintRed, HintWhite, HintWhite)

    val code = Code(inputCode, Vector.empty)
    assert(hintResult == code.compareTo(userInput))
    code.compareTo(userInput) shouldBe hintResult

  }
