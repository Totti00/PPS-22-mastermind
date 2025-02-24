package mastermind

import mastermind.contoller.ControllerModule
import mastermind.model.ModelModule
import mastermind.view.ViewModule
import scalafx.application.JFXApp3

object Launcher extends ModelModule.Interface with ViewModule.Interface with ControllerModule.Interface:

  override val model = new ModelImpl()
  override val view = new ViewImpl()
  override val controller = new ControllerImpl()

  @main def main(): Unit =
    view.print()
    model.print()
    controller.print()
