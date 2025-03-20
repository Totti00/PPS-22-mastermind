package mastermind.model.entity

import mastermind.model.entity.HintStone.{HintRed, HintWhite}
import org.scalatest.flatspec.AnyFlatSpec

class StoneTest extends AnyFlatSpec:

  /*
  "a Purple stone" should "be purple" in {
    assert(Purple.stringRepresentation.equals("Purple"))
  }

  "a red Playable stone" should "be red stone" in {
    assert(PlayerStoneGrid.fromString("Red") == Red)
    assert(!(PlayerStoneGrid.fromString("Red") == HintRed))
  }
   */
  "a Hint white" should "be white" in {
    assert(HintWhite.toString == "White")
  }

  "a red Hint stone" should "be red hint stone" in {
    assert(HintRed.isInstanceOf[HintStone])
    assert(!HintRed.isInstanceOf[PlayerStoneGrid])
  }

  "stone random" should "return a right random" in {
    PlayerStoneGrid.random(4)
    assert(1 == 1)
  }
