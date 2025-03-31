package mastermind.model

import mastermind.Launcher.ModelImpl
import mastermind.model.PlayerLose
import mastermind.model.ModelModule.Model
import mastermind.model.entity.HintStone.{HintEmpty, HintRed}
import mastermind.model.entity.{HintStone, PlayerStone}
import mastermind.model.entity.PlayerStone.{Empty, Playable}
import mastermind.model.mode.{EasyMode, ExtremeMode, MediumMode}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ModelModuleTest extends AnyFlatSpec with Matchers:

  var model: Model = new ModelImpl()

  "startNewGame" should "manage wrong mode" in {
    model.startNewGame("start")

    assert(model.getSizeBoard._1 == MediumMode().boardSize._1)
    assert(model.getSizeBoard._2 == MediumMode().boardSize._2)
    model.getSizeBoard._1 should not be EasyMode().boardSize._1
    model.getSizeBoard._2 should not be EasyMode().boardSize._2
  }

  "startNewGame" should "initialize the game with the correct difficulty" in {
    val mode = EasyMode()
    model.startNewGame(mode.name)
    assert(model.currentTurn == 0)
    model.remainingTurns shouldBe mode.boardSize._1
  }

  "reset" should "restart the game with the same difficulty" in {

    val mode = MediumMode()
    model.startNewGame(mode.name)
    assert(model.currentTurn == 0)
    assert(model.remainingTurns == mode.boardSize._1)

    model.reset()
    assert(model.currentTurn == 0)
    model.remainingTurns shouldBe mode.boardSize._1
  }

  "submitGuess" should "provide correct feedback of hint stones" in {
    val mode = EasyMode()
    model.startNewGame(mode.name)
    val guess =
      Vector(
        PlayerStone.fromString("Green"),
        PlayerStone.fromString("Green"),
        PlayerStone.fromString("White"),
        PlayerStone.fromString("Yellow")
      )
    val feedback = model.submitGuess(guess)

    feedback should not be empty
    feedback.forall(hint => hint.isInstanceOf[HintStone]) shouldBe true
  }

  "startNewTurn" should "increment the current turn" in {
    val mode = MediumMode()
    model.startNewGame(mode.name)
    val initialTurn = model.currentTurn
    model.startNewTurn()
    model.currentTurn shouldBe initialTurn + 1
  }

  it should "mark the game as lose if no turns remain" in {
    val mode = EasyMode()
    model.startNewGame(mode.name)
    val guess =
      Vector(
        PlayerStone.fromString("Green"),
        PlayerStone.fromString("Green"),
        PlayerStone.fromString("White"),
        PlayerStone.fromString("Yellow")
      )
    for _ <- 1 to mode.boardSize._1 do
      model.submitGuess(guess)
      model.startNewTurn()
    model.gameState shouldBe PlayerLose
  }

  "getSizeBoard" should "return the correct board size for the current game" in {
    val mode = MediumMode()
    model.startNewGame(mode.name)
    val (rows, cols) = model.getSizeBoard
    (rows, cols) shouldBe mode.boardSize

  }

  "getPlayableStone" should "return the correct PlayerStoneGrid" in {
    val mode = MediumMode()
    model.startNewGame(mode.name)
    val stone = model.getPlayableStone(0, 0)
    stone shouldBe Playable
    stone should not be HintRed
  }

  "getHintStone" should "return the correct HintStone" in {
    val mode = ExtremeMode()
    model.startNewGame(mode.name)
    val hint = model.getHintStone(0, 0)
    hint should not be Empty
    hint shouldBe HintEmpty
  }
