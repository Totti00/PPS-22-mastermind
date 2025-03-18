package mastermind.model.entity

import mastermind.model.entity.HintStone.*
import org.scalatest.flatspec.AnyFlatSpec

class CodeTest extends AnyFlatSpec:
  /*
//test to check the validity of the rules
  "Code" should "find the stones in the right position" in {
    val inputCode =
      Vector(PlayerStoneGrid("Green"), PlayerStoneGrid("Green"), PlayerStoneGrid("White"), PlayerStoneGrid("Yellow"))
    val userInput =
      Vector(PlayerStoneGrid("Green"), PlayerStoneGrid("Green"), PlayerStoneGrid("Blue"), PlayerStoneGrid("Green"))
    val hintResult = Vector(HintStone("Red"), HintStone("Red"))
    val code = Code(inputCode)

    assert(hintResult == code.compareTo(userInput))
  }

  "Code" should "find the stones in the wrong position" in {
    val inputCode =
      Vector(PlayerStoneGrid("Green"), PlayerStoneGrid("White"), PlayerStoneGrid("White"), PlayerStoneGrid("Yellow"))
    val userInput =
      Vector(PlayerStoneGrid("Green"), PlayerStoneGrid("White"), PlayerStoneGrid("Blue"), PlayerStoneGrid("White"))
    val hintResult = Vector(HintStone("White"))
    val code = Code(inputCode)

    assert(hintResult == code.compareTo(userInput))

  }
   */

  "Code" should "check the user input to find the hintVector" in {
    /*
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

    val inputCode2 =
      Vector(
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("White"),
        PlayerStoneGrid.fromString("White")
      )
    val userInput2 =
      Vector(
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("Yellow")
      )
    val hintResult2 = Vector(HintWhite, HintWhite, HintEmpty, HintEmpty)

    val inputCode3 =
      Vector(
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("White"),
        PlayerStoneGrid.fromString("White")
      )
    val userInput3 =
      Vector(
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("Blue")
      )
    val hintResult3 = Vector(HintWhite, HintWhite, HintEmpty, HintEmpty)

    val inputCode4 =
      Vector(
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("White"),
        PlayerStoneGrid.fromString("White")
      )
    val userInput4 =
      Vector(
        PlayerStoneGrid.fromString("Yellow"),
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Blue"),
        PlayerStoneGrid.fromString("Blue")
      )
    val hintResult4 = Vector(HintRed, HintEmpty, HintEmpty, HintEmpty)

    val code = Code(inputCode)
    val code2 = Code(inputCode2)
    val code3 = Code(inputCode3)
    val code4 = Code(inputCode4)

    assert(hintResult == code.compareTo(userInput))
    assert(hintResult2 == code2.compareTo(userInput2))
    assert(hintResult3 == code3.compareTo(userInput3))
    assert(hintResult4 == code4.compareTo(userInput4))
     */
  }
