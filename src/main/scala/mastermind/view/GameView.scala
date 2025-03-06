package mastermind.view

import mastermind.contoller.ControllerModule
import javafx.fxml.FXMLLoader
import scalafx.scene.Scene
import scalafx.stage.Stage
import javafx.scene.{ImageCursor, Parent}
import javafx.scene.layout.GridPane
import javafx.scene.control.{Button, Label, TextField}
import scalafx.Includes.*
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.ScrollEvent

import scala.jdk.CollectionConverters.*

class GameView(context: ControllerModule.Provider):
  private var attemptGrid: javafx.scene.layout.GridPane = _
  private var hintGrid: javafx.scene.layout.GridPane = _
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
    loader.setController(this)
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

    context.controller.startGame(difficulty) // Inizializza il gioco con la difficoltà scelta
    initializeGrids(attemptGrid, hintGrid)

    import scalafx.Includes.*
    stage.scene = new Scene(root)
    setupScrollHandler(stage.scene.value)
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

  /** Handles submitting the user's guess, extracting and processing it if valid.
    */
  private def submitGuess(): Unit =
    val guess = extractGuess()
    if guess.nonEmpty then
      println("Guess: " + guess)
      // updateHintGrid(hints)

  /** Extracts the current guess from the attempt grid.
    *
    * @return
    *   A vector of strings representing the user's selected colors.
    */
  private def extractGuess(): Vector[String] =
    attemptGrid.getChildren
      .filtered(_.isInstanceOf[TextField])
      .asScala
      .map(_.asInstanceOf[TextField].getText)
      .filter(_.nonEmpty)
      .toVector

  /** Updates the hint grid with feedback from previous guesses.
    * @param hints
    *   A vector of strings representing the hints to display.
    */
  private def updateHintGrid(hints: Vector[String]): Unit =
    val labels = hintGrid.getChildren.filtered(_.isInstanceOf[Label])
    for i <- hints.indices if i < labels.size do labels.get(i).asInstanceOf[Label].setText(hints(i))

  /** Returns a stone label for the given column and row.
    * @param c
    *   The column index.
    * @param r
    *   The row index.
    * @return
    *   A label representing a stone.
    */
  private def getStone(c: Int, r: Int): Label =
    val circle_size = 60
    val image_size = circle_size - 5
    val label = new Label("")
    label.setPrefSize(circle_size, circle_size)
    label.setMinSize(circle_size, circle_size)
    label.setMaxSize(circle_size, circle_size)
    label.setGraphic(
      new ImageView(
        new Image(
          getClass
            .getResource("/img/stones/stone_" + context.controller.getStone(r, c, "playable") + ".png")
            .toExternalForm,
          image_size,
          image_size,
          true,
          true
        )
      )
    )
    label.setOnMouseClicked { _ =>
      // context.controller.updateCell(r,c,selectableColors(browseColors)))

      label.setGraphic(
        new ImageView(
          new Image(
            getClass.getResource("/img/stones/stone_" + selectableColors(browseColors) + ".png").toExternalForm,
            image_size,
            image_size,
            true,
            true
          )
        )
      )
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
    val circle_size = 60
    val image_size = circle_size - 5
    val label = new Label("")
    label.setPrefSize(circle_size, circle_size)
    label.setMinSize(circle_size, circle_size)
    label.setMaxSize(circle_size, circle_size)
    label.setGraphic(
      new ImageView(
        new Image(
          getClass.getResource("/img/hintStones/hstone_E.png").toExternalForm,
          image_size,
          image_size,
          true,
          true
        )
      )
    )
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
