package mastermind.view

import javafx.embed.swing.JFXPanel
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import mastermind.controller.ControllerModule
import mastermind.model.GameState
import mastermind.model.entity.HintStone.{HintRed, HintWhite}
import mastermind.utils.PagesEnum.Rules
import mastermind.utils.{Initialize, UpdateHint, UpdatePlayable}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalafx.application.Platform
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent
import scalafx.stage.Stage
import scala.jdk.CollectionConverters.*

class GameViewTest extends AnyFlatSpec with Matchers with BeforeAndAfterAll with BeforeAndAfterEach:

  override def beforeAll(): Unit =
    new JFXPanel()
    System.setProperty("javafx.platform", "Monocle")
    System.setProperty("monocle.platform", "Headless")

  private var mockController: ControllerModule.Controller = _
  private var mockStage: Stage = _
  private var gameView: GameView = _

  override def beforeEach(): Unit =
    Platform.runLater { () =>
      mockController = mock(classOf[ControllerModule.Controller])
      mockStage = new Stage()
      gameView = GameView(mockController, mockStage)
    }

  private def getPrivateField[T](fieldName: String): T =
    val field = gameView.getClass.getDeclaredField(fieldName)
    field.setAccessible(true)
    field.get(gameView).asInstanceOf[T]

  private def runOnFXThread(testCode: => Unit): Unit =
    Platform.runLater(() => testCode)

  "CheckButton" should "call controller.checkCode" in runOnFXThread:
    when(mockController.gameState).thenReturn(GameState.InGame)
    gameView.checkButton()
    verify(mockController).checkCode(any())

  "ResetButton" should "trigger controller reset" in runOnFXThread:
    gameView.resetButton()
    verify(mockController).resetGame()

  "BackButton" should "trigger controller backToMenu" in runOnFXThread:
    gameView.backButton()
    verify(mockController).backToMenu()

  "HelpButton" should "show a dialog" in runOnFXThread:
    gameView.helpButton()
    verify(mockController).goToPage(Rules, None)

  "UpdateView" should "update the view" in runOnFXThread:
    gameView.updateView(Initialize, None)

  "UpdateView" should "display 'You Win!' when game state is PlayerWin" in runOnFXThread:
    when(mockController.gameState).thenReturn(GameState.PlayerWin)
    gameView.updateView(UpdatePlayable, None)

    val resultGameLabel = getPrivateField[Label]("resultGame")
    resultGameLabel.getText shouldBe "You Win!"

  "UpdateView" should "clear grids and initialize timer on Initialize" in runOnFXThread:
    when(mockController.gameState).thenReturn(GameState.InGame)
    gameView.updateView(Initialize, None)

    val attemptGrid = getPrivateField[GridPane]("attemptGrid")
    val hintGrid = getPrivateField[GridPane]("hintGrid")
    val timeLabel = getPrivateField[Label]("timeLabel")

    assert(attemptGrid.getChildren.size == 40)
    assert(hintGrid.getChildren.size == 40)
    timeLabel.getText shouldBe "Time: 00:00"

  "UpdateView" should "display 'You Lose!' when game state is PlayerLose" in runOnFXThread:
    when(mockController.gameState).thenReturn(GameState.PlayerLose)
    gameView.updateView(Initialize, None)
    getPrivateField[Label]("resultGame").getText shouldBe "You Lose!"

  "UpdateView" should "update hint grid for UpdateHint type" in runOnFXThread:
    when(mockController.gameState).thenReturn(GameState.InGame)
    when(mockController.turn).thenReturn(0)
    val testHints = Vector(HintRed, HintWhite, HintWhite, HintWhite, HintRed)
    gameView.updateView(UpdateHint, Some(testHints))

    val hintGrid = getPrivateField[GridPane]("hintGrid")
    val hintLabels = hintGrid.getChildren.asScala
      .collect { case label: Label => label.getText }

    hintLabels should contain allOf ("Red", "White, White, White", "Red")

  "A mouse click on a playable stone" should "update the label's graphic and text" in runOnFXThread {
    val label = new Label("Playable")
    label.setOnMouseClicked { _ =>
      label.setGraphic(
        new ImageView(new Image(getClass.getResource(s"/img/stones/stone_Red.png").toExternalForm, 55, 55, true, true))
      )
      label.setText("Red")
    }
    val mouseEvent = mock(classOf[MouseEvent])
    label.getOnMouseClicked.handle(mouseEvent)
    assert(label.getText == "Red")
    label.getGraphic should not be null
  }
