package mastermind.view

import mastermind.contoller.ControllerModule

object ViewModule:

  trait View:
    def print(): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class ViewImpl extends View:
      override def print(): Unit = println("Hello World")

  trait Interface extends Provider with Component:
    self: Requirements =>
