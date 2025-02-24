package mastermind.model

object ModelModule:
  trait Model:
    //all the methods
    def print(): Unit

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      def print(): Unit = println("ModelImpl hello")


  trait Interface extends Provider with Component


