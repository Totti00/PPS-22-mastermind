package mastermind.view

import mastermind.contoller.ControllerModule

trait MenuView:
  def easyModeButtonClick(): Unit
  def mediumModeButtonClick(): Unit
  def hardModeButtonClick(): Unit
  def extremeModeButtonClick(): Unit
  def rulesButtonClick(): Unit

object MenuView:
  def apply(controller: ControllerModule.Controller): MenuView = MenuViewImpl(controller)

  private class MenuViewImpl(private val controller: ControllerModule.Controller) extends MenuView:

    // TODO: Sostituire difficoltà con GameMode
    override def easyModeButtonClick(): Unit = controller.goToPage("game", Some("easy"))
    override def mediumModeButtonClick(): Unit = controller.goToPage("game", Some("medium"))
    override def hardModeButtonClick(): Unit = controller.goToPage("game", Some("hard"))
    override def extremeModeButtonClick(): Unit = controller.goToPage("game", Some("extreme"))
    override def rulesButtonClick(): Unit = controller.goToPage("Rules")
