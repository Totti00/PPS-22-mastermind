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
    val userInput1 =
      Vector(PlayerStoneGrid("White"), PlayerStoneGrid("White"), PlayerStoneGrid("White"), PlayerStoneGrid("White"))
    val userInput2 =
      Vector(PlayerStoneGrid("Red"), PlayerStoneGrid("Red"), PlayerStoneGrid("Red"), PlayerStoneGrid("Red"))
    val userInput3 =
      Vector(PlayerStoneGrid("Purple"), PlayerStoneGrid("Purple"), PlayerStoneGrid("Purple"), PlayerStoneGrid("Purple"))
    val userInput4 =
      Vector(PlayerStoneGrid("Yellow"), PlayerStoneGrid("Yellow"), PlayerStoneGrid("Yellow"), PlayerStoneGrid("Yellow"))
    val userInput5 =
      Vector(PlayerStoneGrid("Blue"), PlayerStoneGrid("Blue"), PlayerStoneGrid("Blue"), PlayerStoneGrid("Blue"))
    val userInput6 =
      Vector(PlayerStoneGrid("Green"), PlayerStoneGrid("Green"), PlayerStoneGrid("Green"), PlayerStoneGrid("Green"))

    val hintResult = Vector(HintStone("Red"), HintStone("White"), HintStone("White"), HintStone("Red"))
    val code = Code(inputCode)

    assert(hintResult == code.compareTo(userInput1))
    assert(hintResult != code.compareTo(userInput2))
    assert(hintResult != code.compareTo(userInput3))
    assert(hintResult != code.compareTo(userInput4))
    assert(hintResult != code.compareTo(userInput5))
    assert(hintResult != code.compareTo(userInput6))

    // assert(hintResult == code.compareTo(userInput))

  }
