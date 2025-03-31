package mastermind.view

import javafx.fxml.{Initializable, FXML}
import mastermind.controller.ControllerModule
import mastermind.utils.PagesEnum.*
import mastermind.model.mode.*
import scalafx.scene.Scene
import scalafx.stage.Stage
import javafx.scene.layout.AnchorPane
import java.net.URL
import java.util.ResourceBundle

trait MenuView:
  /** Handles the action when the easy mode button is clicked.
    */
  def easyModeButton(): Unit

  /** Handles the action when the medium mode button is clicked.
    */
  def mediumModeButton(): Unit

  /** Handles the action when the hard mode button is clicked.
    */
  def hardModeButton(): Unit

  /** Handles the action when the extreme mode button is clicked.
    */
  def extremeModeButton(): Unit

  /** Handles the action when the rules button is clicked.
    */
  def rulesButton(): Unit

object MenuView:
  def apply(controller: ControllerModule.Controller, stage: Stage): MenuView = MenuViewImpl(controller, stage)

  private class MenuViewImpl(private val controller: ControllerModule.Controller, stage: Stage)
      extends MenuView
      with Initializable:

    // noinspection VarCouldBeVal
    @FXML
    private var menuContainer: AnchorPane = _

    /** This method is called after the FXML view is loaded.
      *
      * @param url
      *   The URL of the FXML file.
      * @param resourceBundle
      *   The resource bundle used for localization.
      */
    override def initialize(url: URL, resourceBundle: ResourceBundle): Unit =
      import scalafx.Includes.*
      stage.scene = new Scene(menuContainer, 800, 500)
      stage.title = "Mastermind"
      stage.show()

    override def easyModeButton(): Unit = controller.goToPage(Game, Some(EasyMode().name))
    override def mediumModeButton(): Unit = controller.goToPage(Game, Some(MediumMode().name))
    override def hardModeButton(): Unit = controller.goToPage(Game, Some(HardMode().name))
    override def extremeModeButton(): Unit = controller.goToPage(Game, Some(ExtremeMode().name))
    override def rulesButton(): Unit = controller.goToPage(Rules)
