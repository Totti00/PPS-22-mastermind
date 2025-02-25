package mastermind

import mastermind.contoller.ControllerModule
import mastermind.model.ModelModule
import mastermind.view.ViewModule
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage

trait MVC extends ModelModule.Interface with ViewModule.Interface with ControllerModule.Interface

object Launcher extends MVC with JFXApp3:
  override val model = new ModelImpl()
  override val view = new ViewImpl()
  override val controller = new ControllerImpl()

  override def start(): Unit = 
    view.show(PrimaryStage());
