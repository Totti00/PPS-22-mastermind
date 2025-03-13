package mastermind.view

import mastermind.contoller.ControllerModule
import javafx.fxml.FXMLLoader
import scalafx.scene.Scene
import scalafx.stage.Stage
import javafx.scene.{ImageCursor, Parent, control, layout}
import javafx.scene.layout.GridPane
import javafx.scene.control.{Button, Label}
import mastermind.model.GameState
import mastermind.model.entity.{HintStone, PlayerStoneGrid, Stone}
import mastermind.utils.*
import scalafx.Includes.*
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.ScrollEvent
import scalafx.util.Duration

import java.text.{DateFormat, SimpleDateFormat}
import scala.jdk.CollectionConverters.*
import javafx.collections.ObservableMap
import mastermind.model.GameState.{InGame, PlayerLose, PlayerWin}
import mastermind.model.entity.PlayerStoneGrid.StartCurrentTurn

class GameView(context: ControllerModule.Provider):
  private var attemptGrid: Option[GridPane] = None
  private var hintGrid: GridPane = _
  private var turnsLabel: Option[Label] = None
  private var resultGame: Option[Label] = None
  private var timeLabel: Option[Label] = None
  private var browseColors: Int = 0
  private val selectableColors: Vector[String] = Vector("Green", "Red", "Blue", "Yellow", "Purple", "White")
  private var timer: Option[Timeline] = None

  /** Displays the game view, initializing grids, buttons, and setting up the scene.
    *
    * @param stage
    *   The main window to display the game scene.
    * @param difficulty
    *   The chosen difficulty level for the game.
    */
  def show(stage: Stage, difficulty: String): Unit =
    val loader = new FXMLLoader(getClass.getResource("/fxml/Game.fxml"))
    val root: Parent = loader.load()
    val namespace = loader.getNamespace
    attemptGrid = Some(namespace.get("stone_matrix").asInstanceOf[GridPane])
    hintGrid = namespace.get("hint_stone_matrix").asInstanceOf[GridPane]
    turnsLabel = Some(namespace.get("labelCurrentTurn").asInstanceOf[Label])
    resultGame = Some(namespace.get("resultGame").asInstanceOf[Label])
    timeLabel = Some(namespace.get("currentTime").asInstanceOf[Label])
    setupButton(namespace, "resetGameButton", () => context.controller.resetGame(difficulty))
    setupButton(namespace, "backButton", () => context.controller.backToMenu("MenuPage"))
    setupButton(namespace, "checkButton", () => submitGuess())
    setupButton(namespace, "helpButton", () => context.controller.goToPage("Rules"))

    context.controller.startGame(difficulty)
    updateGrids(Initialize)

    stage.scene = new Scene(root)
    setupScrollHandler(stage.scene.value)
    setCustomCursor(stage.scene.value)
    setLabelText(turnsLabel.get, s"Remaining Turns: ${context.controller.remainingTurns}")
    stage.sizeToScene()
    stage.title = "Mastermind"
    stage.show()

  /** Sets up a button.
    * @param namespace
    *   The namespace
    * @param buttonId
    *   The button id
    * @param action
    *   The action to perform
    */
  private def setupButton(namespace: ObservableMap[String, Object], buttonId: String, action: () => Unit): Unit =
    namespace.get(buttonId).asInstanceOf[Button].setOnAction(_ => action())

  /** Sets the custom cursor.
    * @param scene
    *   The scene to set the cursor for.
    */
  private def setCustomCursor(scene: Scene): Unit =
    scene.setCursor(
      new ImageCursor(
        new Image(
          getClass.getResource(s"/img/coursers/courser_${selectableColors(browseColors)}.png").toExternalForm,
          32,
          32,
          true,
          true
        )
      )
    )

  /** Initializes the time label.
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

  /** Returns the graphic representation of a stone.
    * @param stone
    *   The stone to get the graphic for.
    * @return
    *   An ImageView representing the stone.
    */
  private def getGraphic(stone: Stone): ImageView =
    /*val urlStone = context.controller.gameState match
      case GameState.PlayerWin =>
        if stone.isInstanceOf[HintStone] then "/img/hintStones/hstone_Red.png" else "/img/stones/stone_Win.png"
      case _ =>
        if stone.isInstanceOf[HintStone] then s"/img/hintStones/hstone_${stone.toString}.png"
        else s"/img/stones/stone_${stone.toString}.png"*/

    val urlStone =
      if stone.isInstanceOf[HintStone] then s"/img/hintStones/hstone_${stone.toString}.png"
      else s"/img/stones/stone_${stone.toString}.png"
    val circle_size = 60
    val image_size = circle_size - 5
    new ImageView(new Image(getClass.getResource(urlStone).toExternalForm, image_size, image_size, true, true))

  /** Handles submitting the user's guess, extracting and processing it if valid.
    */
  private def submitGuess(): Unit =
    val guess = extractGuess()
    if !guess.contains(StartCurrentTurn) then context.controller.checkCode(guess)

  /** Extracts the current guess from the attempt grid.
    *
    * @return
    *   A vector of strings representing the user's selected colors.
    */
  private def extractGuess(): Vector[PlayerStoneGrid] =
    attemptGrid.get.getChildren
      .filtered(child => GridPane.getRowIndex(child) == context.controller.turn)
      .asScala
      .map(cell => PlayerStoneGrid.fromString(cell.asInstanceOf[Label].getText))
      .toVector

  /** Returns a playable stone label.
    * @param c
    *   The column
    * @param r
    *   The row
    * @return
    *   The created label
    */
  private def getStone(c: Int, r: Int): Label = createStoneLabel(c, r, "playable")

  /** Returns a hint stone label.
    * @param c
    *   The column
    * @param r
    *   The row
    * @return
    *   The created label
    */
  private def getHint(c: Int, r: Int): Label = createStoneLabel(c, r, "hint")

  /** Creates a stone label.
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
    val stone = context.controller.getStone(r, c, stoneType)
    val label = new Label(stone.toString)
    label.setPrefSize(60, 60)
    label.setMinSize(60, 60)
    label.setMaxSize(60, 60)
    label.setGraphic(getGraphic(stone))
    label.setOnMouseClicked { _ =>
      if context.controller.turn == r then
        label.setGraphic(
          new ImageView(
            new Image(
              getClass.getResource(s"/img/stones/stone_${selectableColors(browseColors)}.png").toExternalForm,
              55,
              55,
              true,
              true
            )
          )
        )
        label.setText(selectableColors(browseColors))
    }
    label

  /** Sets up the scroll handler for the scene, allowing the user to change the cursor color.
    * @param scene
    *   The scene to set up the scroll handler for.
    */
  private def setupScrollHandler(scene: Scene): Unit =
    scene.addEventHandler(
      ScrollEvent.Scroll,
      (event: ScrollEvent) =>
        if math.abs(event.deltaY) >= 2 then
          if event.deltaY < 0 then
            browseColors = (browseColors + 1) % selectableColors.length
            setCustomCursor(scene)
    )

  /** Updates the grids based on the given update type.
    * @param updateType
    *   The type of update to perform.
    * @param hintStones
    *   The hint stones to update the hint grid with.
    */
  def updateGrids(updateType: GridUpdateType, hintStones: Option[Vector[HintStone]] = None): Unit =
    updateRemainingTurns()
    val (rows, cols) = context.controller.getSizeBoard
    context.controller.gameState match
      case PlayerWin =>
        timer.get.stop()
        setLabelText(resultGame.get, "You Win!")
        println("GameView: before fillGrid")
        fillGrid(rows, cols)
      case PlayerLose =>
        timer.get.stop()
        setLabelText(resultGame.get, "You Lose!")
      case InGame =>
        updateType match
          case Initialize =>
            attemptGrid.get.getChildren.clear()
            hintGrid.getChildren.clear()
            initializeTime(timeLabel.get)
            setLabelText(resultGame.get, "")
            fillGrid(rows, cols)
          case UpdateHint =>
            updateGrid(hintGrid, hintStones.getOrElse(Vector.empty), getGraphicLabel)
          case UpdatePlayable =>
            val newCurrentTurnStones = (for (i <- 0 until cols) yield getStone(i, context.controller.turn)).toVector
            updateGrid(attemptGrid.get, newCurrentTurnStones, identity)

  /** Fills the grids with stones.
    */
  private def fillGrid(rows: Int, cols: Int): Unit =
    for c <- 0 until cols; r <- 0 until rows do
      println("Game view: " + getStone(c, r).getText)
      attemptGrid.get.add(getStone(c, r), c, r)
      hintGrid.add(getHint(c, r), c, r)

  /** Updates the turns label with the remaining turns.
    */
  private def updateRemainingTurns(): Unit =
    val remainingTurns = context.controller.remainingTurns
    setLabelText(turnsLabel.get, s"Remaining Turns: $remainingTurns")

  /** Sets the text of a label.
    *
    * @param label
    *   The label to set the text of.
    * @param text
    *   The text to set.
    */
  private def setLabelText(label: Label, text: String): Unit =
    label.setText(text)

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
    grid.getChildren
      .filtered(child => GridPane.getRowIndex(child) == context.controller.turn)
      .asScala
      .zip(newValues)
      .foreach { case (label, newValue) =>
        val newLabel = labelTransformer(newValue)
        label.asInstanceOf[Label].setGraphic(newLabel.getGraphic)
        label.asInstanceOf[Label].setText(newLabel.getText)
      }

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
