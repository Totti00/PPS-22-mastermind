package mastermind.view

import mastermind.contoller.ControllerModule
import javafx.fxml.FXMLLoader
import scalafx.scene.Scene
import scalafx.stage.Stage
import javafx.scene.{ImageCursor, Parent}
import javafx.scene.layout.GridPane
import javafx.scene.control.{Button, Label}
import mastermind.model.GameState
import mastermind.model.entity.{HintRed, HintStone, PlayerStoneGrid, Stone}
import mastermind.utils.*
import scalafx.Includes.*
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.ScrollEvent
import scalafx.util.Duration
import java.text.{DateFormat, SimpleDateFormat}
import scala.jdk.CollectionConverters.*
import javafx.collections.ObservableMap

class GameView(context: ControllerModule.Provider):
  private var attemptGrid: GridPane = _
  private var hintGrid: GridPane = _
  private var turnsLabel: Label = _
  private var resultGame: Label = _
  private var timeLabel: Label = _
  private var browseColors: Int = 0
  private val selectableColors: Vector[String] = Vector("Green", "Red", "Blue", "Yellow", "Purple", "White")
  private var timer: Timeline = _

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

    attemptGrid = namespace.get("stone_matrix").asInstanceOf[GridPane]
    hintGrid = namespace.get("hint_stone_matrix").asInstanceOf[GridPane]
    turnsLabel = namespace.get("labelCurrentTurn").asInstanceOf[Label]
    resultGame = namespace.get("resultGame").asInstanceOf[Label]
    timeLabel = namespace.get("currentTime").asInstanceOf[Label]
    setupButton(namespace, "resetGameButton", () => context.controller.resetGame(difficulty))
    setupButton(namespace, "backButton", () => context.controller.goToPage("MenuPage"))
    setupButton(namespace, "checkButton", () => submitGuess())
    setupButton(namespace, "helpButton", () => context.controller.goToPage("Rules"))

    context.controller.startGame(difficulty) // Inizializza il gioco con la difficoltà scelta
    updateGrids(Initialize)

    stage.scene = new Scene(root)
    setupScrollHandler(stage.scene.value)
    setCustomCursor(stage.scene.value)
    setLabelText(turnsLabel, s"Remaining Turns: ${context.controller.remainingTurns}")
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
    val button = namespace.get(buttonId).asInstanceOf[Button]
    button.setOnAction(_ => action())

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
    if timer != null then timer.stop()
    setLabelText(timeLabel, "Time: 00:00")
    val startTime = System.currentTimeMillis()
    val timeFormat: DateFormat = new SimpleDateFormat("mm:ss")
    timer = new Timeline:
      cycleCount = Timeline.Indefinite
      keyFrames = Seq(
        KeyFrame(
          Duration(1000),
          onFinished = () =>
            val diff = System.currentTimeMillis() - startTime
            setLabelText(timeLabel, s"Time: ${timeFormat.format(diff)}")
        )
      )
    timer.play()

  /** Updates the view based on the given hint stones.
    * @param vectorOfHintStones
    *   Optional vector of hint stones to update the view with.
    */
  def updateView(vectorOfHintStones: Option[Vector[HintStone]] = None): Unit =
    val remainingTurns = context.controller.remainingTurns
    if remainingTurns == 0 then
      timer.stop()
      context.controller.gameState_(GameState.PlayerLose)
      setLabelText(resultGame, "You Lose!")
    setLabelText(turnsLabel, s"Remaining Turns: $remainingTurns")

    vectorOfHintStones match
      case None => updateGrids(Initialize)
      case _    => updateGrids(UpdateHint, vectorOfHintStones)

    updateGrids(UpdatePlayable)

  /** Returns the graphic representation of a stone.
    * @param stone
    *   The stone to get the graphic for.
    * @return
    *   An ImageView representing the stone.
    */
  private def getGraphic(stone: Stone): ImageView =
    val urlStone = context.controller.gameState match
      case GameState.PlayerWin =>
        if stone.isInstanceOf[HintStone] then "/img/hintStones/hstone_Red.png" else "/img/stones/stone_Win.png"
      case GameState.PlayerLose =>
        if stone.isInstanceOf[HintStone] then "/img/hintStones/hstone_Empty.png" else "/img/stones/stone_Empty.png"
      case _ =>
        if stone.isInstanceOf[HintStone] then s"/img/hintStones/hstone_${stone.stringRepresentation}.png"
        else s"/img/stones/stone_${stone.stringRepresentation}.png"
    val circle_size = 60
    val image_size = circle_size - 5
    new ImageView(new Image(getClass.getResource(urlStone).toExternalForm, image_size, image_size, true, true))

  /** Handles submitting the user's guess, extracting and processing it if valid.
    */
  private def submitGuess(): Unit =
    val guess = extractGuess()
    if guess.nonEmpty then context.controller.checkCode(guess)

  /** Extracts the current guess from the attempt grid.
    *
    * @return
    *   A vector of strings representing the user's selected colors.
    */
  private def extractGuess(): Vector[PlayerStoneGrid] =
    attemptGrid.getChildren
      .filtered(child => GridPane.getRowIndex(child) == context.controller.turn)
      .asScala
      .map(cell => PlayerStoneGrid(cell.asInstanceOf[Label].getText))
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
    val label = new Label(stone.stringRepresentation)
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
  private def updateGrids(updateType: GridUpdateType, hintStones: Option[Vector[HintStone]] = None): Unit =
    attemptGrid.getChildren.clear()
    hintGrid.getChildren.clear()
    updateType match
      case Initialize =>
        initializeTime(timeLabel)
        setLabelText(resultGame, "")
        fillGrid()
      case UpdateHint =>
        hintStones.foreach { stones =>
          if stones.forall(_ == HintRed) then
            timer.stop()
            context.controller.gameState_(GameState.PlayerWin)
            setLabelText(resultGame, "You Win!")
          fillGrid()
        }
      case UpdatePlayable =>
        fillGrid()

  /** Fills the grids with stones.
    */
  private def fillGrid(): Unit =
    val (rows, cols) = context.controller.getSizeBoard
    for c <- 0 until cols; r <- 0 until rows do
      attemptGrid.add(getStone(c, r), c, r)
      hintGrid.add(getHint(c, r), c, r)

  /** Sets the text of a label.
    * @param label
    *   The label to set the text of.
    * @param text
    *   The text to set.
    */
  private def setLabelText(label: Label, text: String): Unit =
    label.setText(text)