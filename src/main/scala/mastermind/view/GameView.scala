package mastermind.view

import mastermind.contoller.ControllerModule
import javafx.fxml.FXMLLoader
import scalafx.scene.Scene
import scalafx.stage.Stage
import javafx.scene.Parent
import javafx.scene.layout.GridPane
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.image.{Image, ImageView}
import scala.jdk.CollectionConverters.*

class GameView(context: ControllerModule.Provider):
  private var attemptGrid: javafx.scene.layout.GridPane = _
  private var hintGrid: javafx.scene.layout.GridPane = _

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

    context.controller.startGame(difficulty) // Inizializza il gioco con la difficoltà scelta
    initializeGrids(attemptGrid, hintGrid)

    import scalafx.Includes.*
    stage.scene = new Scene(root)
    stage.sizeToScene()
    stage.title = s"Mastermind"
    stage.show()

  private def initializeGrids(attemptGrid: GridPane, hintGrid: GridPane): Unit =
    attemptGrid.getChildren.clear()
    hintGrid.getChildren.clear()
    val rows = 10
    val cols = 4
    val positions = for c <- 0 until cols; r <- 0 until rows yield (c, r)
    positions.foreach { case (c, r) =>
      attemptGrid.add(getStone(c, r), c, r)
      hintGrid.add(getHint(c, r), c, r)
    }

  private def submitGuess(): Unit =
    val guess = extractGuess()
    if guess.nonEmpty then
      println("Guess: " + guess)
      // updateHintGrid(hints)

  private def extractGuess(): Vector[String] =
    attemptGrid.getChildren
      .filtered(_.isInstanceOf[TextField])
      .asScala
      .map(_.asInstanceOf[TextField].getText)
      .filter(_.nonEmpty)
      .toVector

  private def updateHintGrid(hints: Vector[String]): Unit =
    val labels = hintGrid.getChildren.filtered(_.isInstanceOf[Label])
    for i <- hints.indices if i < labels.size do labels.get(i).asInstanceOf[Label].setText(hints(i))

  private def getStone(c: Int, r: Int): Label =
    val circle_size = 60
    val image_size = circle_size - 5
    val label = new Label("")
    label.setPrefSize(circle_size, circle_size)
    label.setMinSize(circle_size, circle_size)
    label.setMaxSize(circle_size, circle_size)
    label.setGraphic(
      new ImageView(
        new Image(getClass.getResource("/img/stones/stone_A.png").toExternalForm, image_size, image_size, true, true)
      )
    )
    label.setOnMouseClicked(_ => println("Coglioooo"))
    label

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
