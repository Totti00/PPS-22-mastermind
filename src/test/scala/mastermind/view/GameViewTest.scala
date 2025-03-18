package mastermind.view

import javafx.embed.swing.JFXPanel
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import mastermind.controller.ControllerModule
import mastermind.model.GameState
import mastermind.model.entity.HintStone.{HintRed, HintWhite}
import mastermind.utils.PagesEnum.{Menu, Rules}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalafx.application.Platform
import mastermind.utils.{Initialize, UpdatePlayable, UpdateHint}
import scalafx.stage.Stage
import scala.jdk.CollectionConverters.*
import java.util.concurrent.CountDownLatch

class GameViewTest extends AnyFlatSpec with Matchers {

  new JFXPanel

  private var mockController: ControllerModule.Controller = _
  private var mockStage: Stage = _
  private var gameView: GameView = _

  private def initializeGameView(): Unit = {
    val latch = new CountDownLatch(1)
    Platform.runLater(() => {
      mockController = mock(classOf[ControllerModule.Controller])
      mockStage = new Stage()
      gameView = GameView(mockController, mockStage)
      latch.countDown()
    })
    latch.await() // Wait for the JavaFX thread to complete initialization
  }

  private def getPrivateField[T](fieldName: String): T = {
    val field = gameView.getClass.getDeclaredField(fieldName)
    field.setAccessible(true) // Ignora i controlli di accesso
    field.get(gameView).asInstanceOf[T]
  }

  "CheckButton" should "call controller.checkCode" in {
    initializeGameView()
    Platform.runLater(() => {
      when(mockController.gameState).thenReturn(GameState.InGame)
      gameView.checkButton()
      verify(mockController).checkCode(any())
    })
  }

  "ResetButton" should "trigger controller reset" in {
    initializeGameView()
    Platform.runLater(() => {
      gameView.resetButton()
      verify(mockController).resetGame()
    })
  }

  "BackButton" should "trigger controller backToMenu" in {
    initializeGameView()
    Platform.runLater(() => {
      gameView.backButton()
      verify(mockController).backToMenu()
    })
  }

  "HelpButton" should "show a dialog" in {
    initializeGameView()
    Platform.runLater(() => {
      gameView.helpButton()
      verify(mockController).goToPage(Rules, None)
    })
  }

  "UpdateView" should "update the view" in {
    initializeGameView()
    Platform.runLater(() => {
      gameView.updateView(Initialize, None)

    })
  }

  "UpdateView" should "display 'You Win!' when game state is PlayerWin" in {
    initializeGameView()
    Platform.runLater(() => {
      when(mockController.gameState).thenReturn(GameState.PlayerWin)

      gameView.updateView(UpdatePlayable, None)

      val resultGameLabel = getPrivateField[Label]("resultGame")
      resultGameLabel.getText shouldBe "You Win!"
    })
  }

  "UpdateView" should "clear grids and initialize timer on Initialize" in {
    initializeGameView()
    Platform.runLater(() => {
      when(mockController.gameState).thenReturn(GameState.InGame)
      gameView.updateView(Initialize, None)
      val attemptGrid = getPrivateField[GridPane]("attemptGrid")
      val hintGrid = getPrivateField[GridPane]("hintGrid")
      val timeLabel = getPrivateField[Label]("timeLabel")

      assert(attemptGrid.getChildren.size == 40)
      assert(hintGrid.getChildren.size == 40)
      timeLabel.getText shouldBe "Time: 00:00"
    })
  }

  "UpdateView" should "display 'You Lose!' when game state is PlayerLose" in {
    initializeGameView()
    Platform.runLater(() => {
      when(mockController.gameState).thenReturn(GameState.PlayerLose)
      gameView.updateView(Initialize, None)
      getPrivateField[Label]("resultGame").getText shouldBe "You Lose!"
    })
  }

  "UpdateView" should "update hint grid for UpdateHint type" in {
    initializeGameView()
    Platform.runLater(() => {
      when(mockController.gameState).thenReturn(GameState.InGame)
      when(mockController.turn).thenReturn(0)
      val testHints = Vector(HintRed, HintWhite, HintWhite, HintWhite, HintRed)

      gameView.updateView(UpdateHint, Some(testHints))

      val hintGrid = getPrivateField[GridPane]("hintGrid")
      val hintLabels = hintGrid.getChildren.asScala
        .filter(_.isInstanceOf[Label])
        .map(_.asInstanceOf[Label])
      hintLabels.map(_.getText) should contain allOf("Red", "White, White, White", "Red")
    })
  }
}
