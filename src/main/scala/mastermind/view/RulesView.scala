package mastermind.view

import javafx.fxml.{FXML, Initializable}
import javafx.scene.layout.AnchorPane
import scalafx.stage.{Popup, Stage}
import java.net.URL
import java.util.ResourceBundle

trait RulesView:
  /** Handles the action when the exit button is clicked.
    */
  def exitButton(): Unit

object RulesView:
  def apply(stage: Stage): RulesView = RulesViewImpl(stage)

  private class RulesViewImpl(stage: Stage) extends RulesView with Initializable:
    private val popup = new Popup()

    // noinspection VarCouldBeVal
    @FXML
    private var infoContainer: AnchorPane = _

    /** This method is called after the FXML view is loaded.
      *
      * @param url
      *   The URL of the FXML file.
      * @param resourceBundle
      *   The resource bundle used for localization.
      */
    override def initialize(url: URL, resourceBundle: ResourceBundle): Unit =
      popup.getContent.clear()
      popup.getContent.add(infoContainer)
      popup.show(stage)

    override def exitButton(): Unit =
      popup.hide()
