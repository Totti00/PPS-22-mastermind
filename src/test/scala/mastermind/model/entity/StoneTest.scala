package mastermind.model.entity

import mastermind.model.entity.HintStone.{HintRed, HintWhite}
import mastermind.model.entity.PlayerStone.{Playable, Win}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class StoneTest extends AnyFlatSpec with Matchers:

  "a Playable stone" should "be Playable" in {
    PlayerStone.fromString("playable") shouldBe Playable
    PlayerStone.fromString("playable") should not be HintWhite
  }

  "a Win stone" should "be a Win" in {
    PlayerStone.fromString("win") shouldBe Win
    PlayerStone.fromString("Red") should not be HintRed

  }

  "a Hint white" should "be white" in {
    HintWhite.toString shouldBe "White"
  }

  "a red Hint stone" should "be HintStone" in {
    HintRed shouldBe a[HintStone]
    HintRed should not be a[PlayerStone]
  }
