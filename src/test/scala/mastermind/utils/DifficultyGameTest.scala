package mastermind.utils

import mastermind.model.strategy.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.shouldEqual

class DifficultyGameTest extends AnyFlatSpec:
  "EasyMode" should "have a board size of (10, 4) and a code length of 4" in:
    val easyMode = new EasyMode
    assert(easyMode.boardSize == (10, 4))
    assert(easyMode.codeAndColorLength == 4)
    easyMode.name shouldEqual "Easy"

  "MediumMode" should "have a board size of (8, 5) and a code length of 5" in:
    val mediumMode = new MediumMode
    assert(mediumMode.boardSize == (8, 5))
    assert(mediumMode.codeAndColorLength == 5)
    mediumMode.name shouldEqual "Medium"

  "HardMode" should "have a board size of (6, 5) and a code length of 5" in:
    val hardMode = new HardMode
    assert(hardMode.boardSize == (6, 5))
    assert(hardMode.codeAndColorLength == 5)
    hardMode.name shouldEqual "Hard"

  "ExtremeMode" should "have a board size of (6, 6) and a code length of 6" in:
    val extremeMode = new ExtremeMode
    assert(extremeMode.boardSize == (6, 6))
    assert(extremeMode.codeAndColorLength == 6)
    extremeMode.name shouldEqual "Extreme"
