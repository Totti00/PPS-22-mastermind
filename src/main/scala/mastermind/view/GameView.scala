package mastermind.view

import javafx.event.EventHandler
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Label
import javafx.scene.layout.{GridPane, VBox}
import mastermind.controller.ControllerModule
import mastermind.model.GameState.{InGame, PlayerLose, PlayerWin}
import mastermind.model.entity.{HintStone, HintStones, PlayableStones, PlayerStoneGrid, Stone}
import mastermind.utils.{GridUpdateType, Initialize, UpdateHint, UpdatePlayable}
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.scene.image.{Image, ImageView}
import scalafx.util.Duration
import scalafx.Includes.*
import scalafx.scene.{ImageCursor, Scene}
import scalafx.scene.input.ScrollEvent
import scalafx.stage.Stage
import mastermind.model.entity.PlayerStoneGrid.StartCurrentTurn
import mastermind.utils.PagesEnum.{Menu, Rules}
import java.net.URL
import java.util.ResourceBundle
import scala.jdk.CollectionConverters.*
import java.text.{DateFormat, SimpleDateFormat}

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
      selectableColors = Some(controller.colors)
      setupScrollHandler(stage.scene.get())
      setCustomCursor(stage.scene.get())
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
            case UpdateHint =>
              updateGrid(hintGrid, hintStones.getOrElse(Vector.empty), getGraphicLabel)
            case UpdatePlayable =>
              val newCurrentTurnStones = (for (i <- 0 until cols) yield getStone(i, controller.turn)).toVector
              updateGrid(attemptGrid, newCurrentTurnStones, identity)

    /** Updates the turns label with the remaining turns.
      */
    private def updateRemainingTurns(): Unit =
      setLabelText(turnsLabel, s"Remaining Turns: ${controller.remainingTurns}")

    /** Updates the given grid with new values.
      *
      * @param grid
      *   The `GridPane` to update.
      * @param newValues
      *   A vector containing the new values to display.
      * @param labelTransformer
      *   A function that transforms a value into a `Label`.
      */
    private def updateGrid[T](grid: GridPane, newValues: Vector[T], labelTransformer: T => Label): Unit =
      grid.getChildren.asScala
        .collect { case label: Label if GridPane.getRowIndex(label) == controller.turn => label }
        .zip(newValues)
        .foreach { case (label, newValue) =>
          val newLabel = labelTransformer(newValue)
          label.setGraphic(newLabel.getGraphic)
          label.setText(newLabel.getText)
        }

    /** Initializes the time label.
      *
      * @param timeLabel
      *   The time label to initialize.
      */
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

    /** Creates a label representing a hint stone.
      *
      * @param hintStone
      *   The `HintStone` to represent.
      * @return
      *   A `Label` with the hint stone's text and corresponding graphic.
      */
    private def getGraphicLabel(hintStone: HintStone): Label =
      val label = new Label(hintStone.toString)
      label.setGraphic(getGraphic(hintStone))
      label

    /** Returns the graphic representation of a stone.
      *
      * @param stone
      *   The stone to get the graphic for.
      * @return
      *   An ImageView representing the stone.
      */
    private def getGraphic(stone: Stone): ImageView =
      val urlStone =
        if stone.isInstanceOf[HintStone] then s"/img/hintStones/hstone_${stone.toString}.png"
        else s"/img/stones/stone_${stone.toString}.png"
      val circle_size = 60
      val image_size = circle_size - 5
      new ImageView(new Image(getClass.getResource(urlStone).toExternalForm, image_size, image_size, true, true))

    /** Sets the text of a label.
      *
      * @param label
      *   The label to set the text of.
      * @param text
      *   The text to set.
      */
    private def setLabelText(label: Label, text: String): Unit =
      label.setText(text)

    /** Fills the grids with stones.
      */
    private def fillGrid(rows: Int, cols: Int): Unit =
      for c <- 0 until cols; r <- 0 until rows do
        attemptGrid.add(getStone(c, r), c, r)
        hintGrid.add(getHint(c, r), c, r)

    /** Returns a playable stone label.
      *
      * @param c
      *   The column
      * @param r
      *   The row
      * @return
      *   The created label
      */
    private def getStone(c: Int, r: Int): Label = createStoneLabel(c, r, "playable")

    /** Returns a hint stone label.
      *
      * @param c
      *   The column
      * @param r
      *   The row
      * @return
      *   The created label
      */
    private def getHint(c: Int, r: Int): Label = createStoneLabel(c, r, "hint")

    /** Creates a stone label.
      *
      * @param c
      *   The column
      * @param r
      *   The row
      * @param stoneType
      *   The type of stone
      * @return
      *   The created label
      */
    private def createStoneLabel(c: Int, r: Int, stoneType: String): Label =
      val stone = controller.getStone(r, c, stoneType)
      val label = new Label(stone.toString)
      label.setPrefSize(60, 60)
      label.setMinSize(60, 60)
      label.setMaxSize(60, 60)
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

    /** Sets up the scroll handler for the scene, allowing the user to change the cursor color.
      *
      * @param scene
      *   The scene to set up the scroll handler for.
      */
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

    /** Sets the custom cursor.
      *
      * @param scene
      *   The scene to set the cursor for.
      */
    private def setCustomCursor(scene: Scene): Unit =
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

    /** Handles submitting the user's guess, extracting and processing it if valid.
      */
    private def submitGuess(): Unit =
      if controller.gameState == InGame then
        val guess = extractGuess()
        if !guess.contains(StartCurrentTurn) then controller.checkCode(guess)

    /** Extracts the current guess from the attempt grid.
      *
      * @return
      *   A vector of strings representing the user's selected colors.
      */
    private def extractGuess(): PlayableStones =
      attemptGrid.getChildren
        .filtered(child => GridPane.getRowIndex(child) == controller.turn)
        .asScala
        .map(cell => PlayerStoneGrid.fromString(cell.asInstanceOf[Label].getText))
        .toVector
