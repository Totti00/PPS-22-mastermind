package mastermind.model.entity

import org.scalatest.flatspec.AnyFlatSpec

class StoneTest extends AnyFlatSpec:

  "a Purple stone" should "be purple" in {
    assert(Purple.stringRepresentation.equals("Purple"))
  }

  "a red Playable stone" should "be red stone" in {
    assert(PlayableStone("Red") == Red)
    assert(!(PlayableStone("Red") == HintRed))
  }

  "a Hint white" should "be white" in {
    assert(HintWhite.stringRepresentation.equals("White"))
  }

  "a red Hint stone" should "be red hint stone" in {
    assert(HintStone("Red") == HintRed)
    assert(!(HintStone("Red") == Red))
  }
