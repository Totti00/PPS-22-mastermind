package mastermind.model.entity
//TODO capire se posso vedere code come una variabile e fare override del compareTo associato. Sono quasi sicuro si possa fare e che sia più elegante come soluzione
trait Code:
  def compareTo(userInput: Vector[PlayableStone]): Vector[HintStone]

object Code:
  private type SecretCode = Vector[PlayableStone]
  private type PlayableCode = Vector[PlayableStone]

  def apply(size: Int): Code = CodeImpl(Vector.fill(size)(PlayableStone.random))

  private case class CodeImpl(code: SecretCode) extends Code:
    override def compareTo(userInput: PlayableCode): Vector[HintStone] =
      println(code)
      println(userInput)
      val testHintRow = Vector(HintStone("Red"), HintStone("White"), HintStone("White"), HintStone("Empty"))
      testHintRow


    