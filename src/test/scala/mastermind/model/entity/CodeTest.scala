package mastermind.model.entity

import org.scalatest.flatspec.AnyFlatSpec

class CodeTest extends AnyFlatSpec:

  /*
//test to check the validity of the rules
  "Code" should "find the stones in the right position" in {
    val inputCode = Vector(PlayerStoneGrid("Yellow"), PlayerStoneGrid("White"), PlayerStoneGrid("White"), PlayerStoneGrid("Yellow"))
    val userInput =
      Vector(PlayerStoneGrid("Red"), PlayerStoneGrid("Blue"), PlayerStoneGrid("Green"), PlayerStoneGrid("Yellow"))
    val hintResult = Vector(HintStone("Empty"), HintStone("Empty"), HintStone("Empty"), HintStone("White"))
    val code = Code(inputCode)

    assert(hintResult == code.compareTo(userInput))


  }

  "Code" should "find the stones in the wrong position" in {
    val inputCode = Vector(PlayerStoneGrid("Green"), PlayerStoneGrid("White"), PlayerStoneGrid("White"), PlayerStoneGrid("Yellow"))
    val userInput =
      Vector(PlayerStoneGrid("Red"), PlayerStoneGrid("Blue"), PlayerStoneGrid("Green"), PlayerStoneGrid("Yellow"))
    val hintResult = Vector(HintStone("Empty"), HintStone("Empty"), HintStone("Red"), HintStone("Empty"))
    val code = Code(inputCode)

    assert(hintResult == code.compareTo(userInput))


  }
   */
  "Code" should "check the user input to find the hintVector" in {
    val inputCode =
      Vector(PlayerStoneGrid("Green"), PlayerStoneGrid("White"), PlayerStoneGrid("White"), PlayerStoneGrid("Yellow"))
    val userInput =
      Vector(PlayerStoneGrid("Red"), PlayerStoneGrid("Blue"), PlayerStoneGrid("Green"), PlayerStoneGrid("Yellow"))
    val hintResult = Vector(HintStone("Empty"), HintStone("Empty"), HintStone("Red"), HintStone("White"))
    val code = Code(inputCode)

    assert(hintResult == code.compareTo(userInput))

  }
