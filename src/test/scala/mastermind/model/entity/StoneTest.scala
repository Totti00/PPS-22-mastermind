package mastermind.model.entity

import mastermind.model.entity.HintStone.{HintRed, HintWhite}
import mastermind.model.entity.PlayerStoneGrid.{Playable, Win}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class StoneTest extends AnyFlatSpec with Matchers:

  "a Playable stone" should "be Playable" in {
    PlayerStoneGrid.fromString("playable") shouldBe Playable
    PlayerStoneGrid.fromString("playable") should not be HintWhite
  }

  "a Win stone" should "be a Win" in {
    PlayerStoneGrid.fromString("win") shouldBe Win
    PlayerStoneGrid.fromString("Red") should not be HintRed

  }

  "a Hint white" should "be white" in {
    HintWhite.toString shouldBe "White"
  }

  "a red Hint stone" should "be HintStone" in {
    HintRed shouldBe a[HintStone]
    HintRed should not be a[PlayerStoneGrid]
  }
