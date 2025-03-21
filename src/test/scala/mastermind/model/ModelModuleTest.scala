package mastermind.model

import mastermind.Launcher.ModelImpl
import mastermind.model.GameState.PlayerLose
import mastermind.model.ModelModule.Model
import mastermind.model.entity.HintStone.{HintEmpty, HintRed, HintWhite}
import mastermind.model.entity.PlayerStoneGrid
import mastermind.model.entity.PlayerStoneGrid.{Playable, Empty}
import mastermind.model.strategy.{EasyMode, ExtremeMode, MediumMode}
import org.scalatest.flatspec.AnyFlatSpec

class ModelModuleTest extends AnyFlatSpec:

  var model: Model = new ModelImpl()

  "startNewGame" should "initialize the game with the correct difficulty" in {
    val mode = EasyMode()
    model.startNewGame(mode.name)
    assert(model.currentTurn == 0)
    assert(model.remainingTurns == mode.boardSize._1)
  }

  "reset" should "restart the game with the same difficulty" in {

    val mode = MediumMode()
    model.startNewGame(mode.name)
    assert(model.currentTurn == 0)
    assert(model.remainingTurns == mode.boardSize._1)

    model.reset()
    assert(model.currentTurn == 0)
    assert(model.remainingTurns == mode.boardSize._1)
  }

  "submitGuess" should "provide correct feedback of hint stones" in {
    val mode = EasyMode()
    model.startNewGame(mode.name)
    val guess =
      Vector(
        PlayerStoneGrid.fromString("Green"),
        PlayerStoneGrid.fromString("Green"),
        PlayerStoneGrid.fromString("White"),
        PlayerStoneGrid.fromString("Yellow")
      )
    val feedback = model.submitGuess(guess)
    assert(feedback.nonEmpty)
    assert(feedback.forall(hint => hint == HintRed || hint == HintWhite || hint == HintEmpty))
  }

  "startNewTurn" should "increment the current turn" in {
    val mode = MediumMode()
    model.startNewGame(mode.name)
    val initialTurn = model.currentTurn
    model.startNewTurn()
    assert(model.currentTurn == initialTurn + 1)
  }

  it should "mark the game as lose if no turns remain" in {
    val mode = EasyMode()
    model.startNewGame(mode.name)
    val guess =
      Vector(
        PlayerStoneGrid.fromString("Green"),
        PlayerStoneGrid.fromString("Green"),
        PlayerStoneGrid.fromString("White"),
        PlayerStoneGrid.fromString("Yellow")
      )
    for _ <- 1 to mode.boardSize._1 do
      model.submitGuess(guess)
      model.startNewTurn()
    assert(model.gameState == PlayerLose)
  }

  /*"deleteGame" should "remove the current game instance" in {
    val mode = EasyMode()
    model.startNewGame(mode.name)
    val status = model.deleteGame()
    assert(status.isEmpty)
  }*/

  "getSizeBoard" should "return the correct board size for the current game" in {
    val mode = MediumMode()
    model.startNewGame(mode.name)
    val (rows, cols) = model.getSizeBoard
    assert((rows, cols) == mode.boardSize)

  }

  "getPlayableStone" should "return the correct PlayerStoneGrid" in {
    val mode = MediumMode()
    model.startNewGame(mode.name)
    val stone = model.getPlayableStone(0, 0)
    assert(stone == Playable)
    assert(stone != HintRed)
  }

  "getHintStone" should "return the correct HintStone" in {
    val mode = ExtremeMode()
    model.startNewGame(mode.name)
    val hint = model.getHintStone(0, 0)
    assert(hint != Empty)
    assert(hint == HintEmpty)
  }
