package mastermind.view

import javafx.event.EventHandler
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Label
import javafx.scene.layout.{GridPane, VBox}
import mastermind.controller.ControllerModule
import mastermind.model.{InGame, PlayerLose, PlayerWin}
import mastermind.model.entity.{HintStone, HintStones, PlayableStones, PlayerStone, Stone}
import mastermind.utils.{GridUpdateType, Initialize, UpdateHint, UpdatePlayable}
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.scene.image.{Image, ImageView}
import scalafx.util.Duration
import scalafx.Includes.*
import scalafx.scene.{ImageCursor, Scene}
import scalafx.scene.input.ScrollEvent
import scalafx.stage.Stage
import mastermind.model.entity.PlayerStone.Playable
import mastermind.utils.PagesEnum.Rules
import java.net.URL
import java.util.ResourceBundle
import scala.jdk.CollectionConverters.*
import java.text.{DateFormat, SimpleDateFormat}
import mastermind.utils.ErrorHandler.*

trait GameView:
  /** Handles the action for the check button, submitting the current guess.
    */
  def checkButton(): Unit

  /** Handles the action for the reset button, resetting the game.
    */
  def resetButton(): Unit

  /** Handles the action for the back button, returning to the main menu.
    */
  def backButton(): Unit

  /** Handles the action for the help button, displaying the rules.
    */
  def helpButton(): Unit

  /** Updates the view based on the given update type.
    * @param updateType
    *   The type of update to perform.
    * @param hintStones
    *   The hint stones to update the hint grid with.
    */
  def updateView(updateType: GridUpdateType, hintStones: Option[HintStones]): Unit

object GameView:
  def apply(controller: ControllerModule.Controller, stage: Stage): GameView = GameViewImpl(controller, stage)
  private class GameViewImpl(private val controller: ControllerModule.Controller, private val stage: Stage)
      extends GameView
      with Initializable:

    // noinspection VarCouldBeVal
    @FXML
    private var resultGame: Label = _

    // noinspection VarCouldBeVal
    @FXML
    private var attemptGrid: GridPane = _

    // noinspection VarCouldBeVal
    @FXML
    private var hintGrid: GridPane = _

    // noinspection VarCouldBeVal
    @FXML
    private var timeLabel: Label = _

    // noinspection VarCouldBeVal
    @FXML
    private var turnsLabel: Label = _

    // noinspection VarCouldBeVal
    @FXML
    private var mainContainer: VBox = _

    private var timer: Option[Timeline] = None
    private var browseColors: Int = 0
    private var selectableColors: Option[PlayableStones] = None

    /** This method is called after the FXML view is loaded.
      *
      * @param location
      *   The URL of the FXML file.
      * @param resourceBundle
      *   The resource bundle used for localization.
      */
    override def initialize(location: URL, resourceBundle: ResourceBundle): Unit =
      updateView(Initialize)
      stage.scene = new Scene(mainContainer)
      setCustomCursor(stage.scene.get())
      setupScrollHandler(stage.scene.get())
      setLabelText(turnsLabel, s"Remaining Turns: ${controller.remainingTurns}")
      stage.sizeToScene()
      stage.title = "Mastermind"
      stage.show()

    override def checkButton(): Unit = submitGuess()

    override def resetButton(): Unit = controller.resetGame()

    override def backButton(): Unit = controller.backToMenu()

    override def helpButton(): Unit = controller.goToPage(Rules)

    override def updateView(updateType: GridUpdateType, hintStones: Option[HintStones] = None): Unit =
      updateRemainingTurns()
      val (rows, cols) = controller.getSizeBoard
      controller.gameState match
        case PlayerWin =>
          timer.get.stop()
          setLabelText(resultGame, "You Win!")
          fillGrid(rows, cols)
        case PlayerLose =>
          timer.get.stop()
          setLabelText(resultGame, "You Lose!")
        case InGame =>
          updateType match
            case Initialize =>
              attemptGrid.getChildren.clear()
              hintGrid.getChildren.clear()
              initializeTime(timeLabel)
              setLabelText(resultGame, "")
              fillGrid(rows, cols)
              setCustomCursor(stage.scene.get())
            case UpdateHint =>
              updateGrid(hintGrid, hintStones.getOrElse(Vector.empty), getGraphicLabel)
            case UpdatePlayable =>
              val newCurrentTurnStones = (for (i <- 0 until cols) yield getStone(i, controller.turn)).toVector
              updateGrid(attemptGrid, newCurrentTurnStones, identity)

    private def updateRemainingTurns(): Unit =
      setLabelText(turnsLabel, s"Remaining Turns: ${controller.remainingTurns}")

    private def updateGrid[T](grid: GridPane, newValues: Vector[T], labelTransformer: T => Label): Unit =
      grid.getChildren.asScala
        .collect { case label: Label if GridPane.getRowIndex(label) == controller.turn => label }
        .zip(newValues)
        .foreach { case (label, newValue) =>
          val newLabel = labelTransformer(newValue)
          label.setGraphic(newLabel.getGraphic)
          label.setText(newLabel.getText)
        }

    private def initializeTime(timeLabel: Label): Unit =
      if timer.isDefined then timer.get.stop()
      setLabelText(timeLabel, "Time: 00:00")
      val startTime = System.currentTimeMillis()
      val timeFormat: DateFormat = new SimpleDateFormat("mm:ss")
      timer = Some(
        new Timeline:
          cycleCount = Timeline.Indefinite
          keyFrames = Seq(
            KeyFrame(
              Duration(1000),
              onFinished = () =>
                val diff = System.currentTimeMillis() - startTime
                setLabelText(timeLabel, s"Time: ${timeFormat.format(diff)}")
            )
          )
      )
      timer.get.play()

    private def getGraphicLabel(hintStone: HintStone): Label =
      val label = new Label(hintStone.toString)
      label.setGraphic(getGraphic(hintStone))
      label

    private def getGraphic(stone: Stone): ImageView =
      val urlStone =
        if stone.isInstanceOf[HintStone] then s"/img/hintStones/hstone_${stone.toString}.png"
        else s"/img/stones/stone_${stone.toString}.png"
      val circle_size = 60
      val image_size = circle_size - 5
      new ImageView(new Image(getClass.getResource(urlStone).toExternalForm, image_size, image_size, true, true))

    private def setLabelText(label: Label, text: String): Unit =
      label.setText(text)

    private def fillGrid(rows: Int, cols: Int): Unit =
      for c <- 0 until cols; r <- 0 until rows do
        attemptGrid.add(getStone(c, r), c, r)
        hintGrid.add(getHint(c, r), c, r)

    private def getStone(c: Int, r: Int): Label = createStoneLabel(c, r, "playable")

    private def getHint(c: Int, r: Int): Label = createStoneLabel(c, r, "hint")

    private def createStoneLabel(c: Int, r: Int, stoneType: String): Label =
      controller.getStone(r, c, stoneType) match
        case Right(stone) =>
          val label = new Label(stone.toString)
          configureLabel(label)
          label.setGraphic(getGraphic(stone))
          label.setOnMouseClicked { _ =>
            if controller.turn == r && stoneType == "playable" then
              label.setGraphic(
                new ImageView(
                  new Image(
                    getClass.getResource(s"/img/stones/stone_${selectableColors.get(browseColors)}.png").toExternalForm,
                    55,
                    55,
                    true,
                    true
                  )
                )
              )
              label.setText(selectableColors.get(browseColors).toString)
          }
          label
        case Left(_) =>
          val label = new Label("Invalid stone type")
          configureLabel(label)
          setLabelText(resultGame, "Stone Type Error !")
          label

    private def configureLabel(label: Label): Label =
      label.setPrefSize(60, 60)
      label.setMinSize(60, 60)
      label.setMaxSize(60, 60)
      label

    private def setupScrollHandler(scene: Scene): Unit =
      scene.addEventHandler(
        ScrollEvent.Scroll,
        (event: ScrollEvent) =>
          if math.abs(event.deltaY) >= 2 then
            if event.deltaY < 0 then browseColors = (browseColors + 1) % selectableColors.get.length
            else if event.deltaY > 0 then
              browseColors = (browseColors - 1 + selectableColors.get.length) % selectableColors.get.length
          setCustomCursor(scene)
      )

    private def setCustomCursor(scene: Scene): Unit =
      selectableColors = Some(controller.colors)
      scene.setCursor(
        new ImageCursor(
          new Image(
            getClass.getResource(s"/img/coursers/courser_${selectableColors.get(browseColors)}.png").toExternalForm,
            32,
            32,
            true,
            true
          )
        )
      )

    private def submitGuess(): Unit =
      if controller.gameState == InGame then
        val guess = extractGuess()
        if !guess.contains(Playable) then controller.checkCode(guess)

    private def extractGuess(): PlayableStones =
      attemptGrid.getChildren
        .filtered(child => GridPane.getRowIndex(child) == controller.turn)
        .asScala
        .map(cell => PlayerStone.fromString(cell.asInstanceOf[Label].getText))
        .toVector
