package mastermind.contoller

import mastermind.model.ModelModule
import mastermind.view.ViewModule

object ControllerModule:

  trait Controller:
    def print(): Unit

  trait Provider:
    val controller: Controller

  type Requirements = ModelModule.Provider with ViewModule.Provider

  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      override def print(): Unit = println("Hello World")

  trait Interface extends Provider with Component:
    self: Requirements =>
