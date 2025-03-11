package mastermind.view

import mastermind.contoller.ControllerModule
import javafx.fxml.FXMLLoader
import scalafx.scene.Scene
import scalafx.stage.Stage
import javafx.scene.{ImageCursor, Parent}
import javafx.scene.layout.GridPane
import javafx.scene.control.{Button, Label, TextField}
import mastermind.model.entity.{HintStone, PlayerStoneGrid, Stone}
import scalafx.Includes.*
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.ScrollEvent

import scala.jdk.CollectionConverters.*

class GameView(context: ControllerModule.Provider):
  private var attemptGrid: GridPane = _
  private var hintGrid: GridPane = _
  private var turnsLabel: Label = _
  private var browseColors: Int = 0
  private val selectableColors: Vector[String] = Vector("Green", "Red", "Blue", "Yellow", "Purple", "White")

  /** Displays the game view, initializing grids, buttons, and setting up the scene.
    *
    * @param stage
    *   The main window to display the game scene.
    * @param difficulty
    *   The chosen difficulty level for the game.
    */
  def show(stage: Stage, difficulty: String): Unit =
    val loader = new FXMLLoader(getClass.getResource("/fxml/Game.fxml"))
    // loader.setController(this)
    val root: Parent = loader.load()

    val namespace = loader.getNamespace
    attemptGrid = namespace.get("stone_matrix") match
      case grid: GridPane => grid
      case _              => throw new ClassCastException

    hintGrid = namespace.get("hint_stone_matrix") match
      case grid: GridPane => grid
      case _              => throw new ClassCastException

    namespace.get("resetGameButton") match
      case button: Button => button.setOnAction(_ => context.controller.resetGame())
      case _              =>

    namespace.get("backButton") match
      case button: Button => button.setOnAction(_ => context.controller.goToPage("MenuPage"))
      case _              =>

    namespace.get("checkButton") match
      case button: Button => button.setOnAction(_ => submitGuess())
      case _              =>

    namespace.get("helpButton") match
      case button: Button => button.setOnAction(_ => context.controller.goToPage("Rules"))
      case _              =>

    turnsLabel = namespace.get("labelCurrentTurn") match
      case label: Label => label
      case _            => throw new ClassCastException

    context.controller.startGame(difficulty) // Inizializza il gioco con la difficoltà scelta
    initializeGrids(attemptGrid, hintGrid)

    // import scalafx.Includes.*
    stage.scene = new Scene(root)
    setupScrollHandler(stage.scene.value)
    stage.scene.value.setCursor(
      new ImageCursor(
        new Image(
          getClass.getResource("/img/coursers/courser_" + selectableColors(browseColors) + ".png").toExternalForm,
          32,
          32,
          true,
          true
        )
      )
    )
    turnsLabel.setText("Remaining Turns: " + context.controller.remainingTurns)
    stage.sizeToScene()
    stage.title = "Mastermind"
    stage.show()

  /** Initializes the attempt and hint grids with labels representing stones and hints.
    *
    * @param attemptGrid
    *   The attempt grid to initialize.
    * @param hintGrid
    *   The hint grid to initialize.
    */
  private def initializeGrids(attemptGrid: GridPane, hintGrid: GridPane): Unit =
    attemptGrid.getChildren.clear()
    hintGrid.getChildren.clear()
    val rows = context.controller.getSizeBoard._1
    val cols = context.controller.getSizeBoard._2

    val positions = for c <- 0 until cols; r <- 0 until rows yield (c, r)
    positions.foreach { case (c, r) =>
      attemptGrid.add(getStone(c, r), c, r)
      hintGrid.add(getHint(c, r), c, r)
    }

  def updateView(vectorOfHintStones: Vector[HintStone]): Unit =
    hintGrid.getChildren
      .filtered(child => GridPane.getRowIndex(child) == context.controller.turn)
      .asScala
      .zip(vectorOfHintStones)
      .foreach { case (label, hintStone) =>
        label.asInstanceOf[Label].setGraphic(getGraphic(hintStone))
        label.asInstanceOf[Label].setText(hintStone.stringRepresentation)
      }

  def updatePlayableView(): Unit =
    attemptGrid.getChildren
      .filtered(child => GridPane.getRowIndex(child) == context.controller.turn)
      .asScala
      .map(child => child.asInstanceOf[Label])
      .zipWithIndex
      .foreach { case (label, index) =>
        val newLabel = getStone(index, context.controller.turn)
        label.setGraphic(newLabel.getGraphic)
        label.setText(newLabel.getText)
      }

  private def getGraphic(stone: Stone): ImageView =
    val urlStone = stone match
      case stone if stone.isInstanceOf[HintStone] => "/img/hintStones/hstone_" + stone.stringRepresentation + ".png"
      case _                                      => "/img/stones/stone_" + stone.stringRepresentation + ".png"

    val circle_size = 60
    val image_size = circle_size - 5
    new ImageView(
      new Image(
        getClass
          .getResource(urlStone)
          .toExternalForm,
        image_size,
        image_size,
        true,
        true
      )
    )

  /** Handles submitting the user's guess, extracting and processing it if valid.
    */
  private def submitGuess(): Unit =
    println("checkcode pressed")
    val guess = extractGuess()
    if guess.nonEmpty then
      println("Guess: " + guess)
      context.controller.checkCode(guess)

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

  /** Returns a stone label for the given column and row.
    * @param c
    *   The column index.
    * @param r
    *   The row index.
    * @return
    *   A label representing a stone.
    */
  private def getStone(c: Int, r: Int): Label =
    val stone = context.controller.getStone(r, c, "playable")
    val circle_size = 60
    val image_size = circle_size - 5
    val label = new Label(stone.stringRepresentation)
    label.setPrefSize(circle_size, circle_size)
    label.setMinSize(circle_size, circle_size)
    label.setMaxSize(circle_size, circle_size)
    label.setGraphic(getGraphic(stone))

    label.setOnMouseClicked { _ =>
      if context.controller.turn == r
      then
        label.setGraphic(
          new ImageView(
            new Image(
              getClass
                .getResource(
                  "/img/stones/stone_" + selectableColors(browseColors) + ".png"
                )
                .toExternalForm,
              image_size,
              image_size,
              true,
              true
            )
          )
        )
      label.setText(selectableColors(browseColors))
    }
    label

  /** Returns a hint label for the given column and row.
    * @param c
    *   The column index.
    * @param r
    *   The row index.
    * @return
    *   A label representing a hint.
    */
  private def getHint(c: Int, r: Int): Label =
    val hintStone = context.controller.getStone(r, c, "hint")
    val circle_size = 60
    val image_size = circle_size - 5
    val label = new Label(hintStone.stringRepresentation)
    label.setPrefSize(circle_size, circle_size)
    label.setMinSize(circle_size, circle_size)
    label.setMaxSize(circle_size, circle_size)
    label.setGraphic(getGraphic(hintStone))
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
            val newCursorImage = new Image(
              getClass.getResource("/img/coursers/courser_" + selectableColors(browseColors) + ".png").toExternalForm,
              32,
              32,
              true,
              true
            )
            scene.setCursor(new ImageCursor(newCursorImage))
    )
