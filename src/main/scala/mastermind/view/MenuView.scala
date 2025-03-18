package mastermind.view

import javafx.fxml.{Initializable, FXML}
import mastermind.controller.ControllerModule
import mastermind.utils.PagesEnum.*
import mastermind.model.strategy.*
import scalafx.scene.Scene
import scalafx.stage.Stage
import javafx.scene.layout.AnchorPane

import java.net.URL
import java.util.ResourceBundle

trait MenuView:
  def easyModeButton(): Unit
  def mediumModeButton(): Unit
  def hardModeButton(): Unit
  def extremeModeButton(): Unit
  def rulesButton(): Unit

object MenuView:
  def apply(controller: ControllerModule.Controller, stage: Stage): MenuView = MenuViewImpl(controller, stage)

  private class MenuViewImpl(private val controller: ControllerModule.Controller, stage: Stage)
      extends MenuView
      with Initializable:

    // noinspection VarCouldBeVal
    @FXML
    private var menuContainer: AnchorPane = _

    override def initialize(url: URL, resourceBundle: ResourceBundle): Unit =
      import scalafx.Includes.*
      stage.scene = new Scene(menuContainer, 800, 500)
      stage.title = "Mastermind"
      stage.show()

    override def easyModeButton(): Unit = controller.goToPage(Game, Some(EasyMode().name)) //
    override def mediumModeButton(): Unit = controller.goToPage(Game, Some(MediumMode().name))
    override def hardModeButton(): Unit = controller.goToPage(Game, Some(HardMode().name))
    override def extremeModeButton(): Unit = controller.goToPage(Game, Some(ExtremeMode().name))
    override def rulesButton(): Unit = controller.goToPage(Rules)
