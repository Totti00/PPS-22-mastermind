package mastermind.model.entity
//TODO capire se posso vedere code come una variabile e fare override del compareTo associato. Sono quasi sicuro si possa fare e che sia più elegante come soluzione
trait Code:
  def compareTo(userInput: Vector[PlayableStone]): Vector[HintStone]

object Code:
  private type SecretCode = Vector[PlayableStone]
  private type PlayableCode = Vector[PlayableStone]
  private type HintCode = Vector[HintStone]

  def apply(size: Int): Code = CodeImpl(Vector.fill(size)(PlayableStone.random))

  private case class CodeImpl(code: SecretCode) extends Code:
    override def compareTo(userInput: PlayableCode): Vector[HintStone] =
      val equalsList = compareToEqual(userInput)
      val presentList = compareToPresent(userInput, equalsList)
      buildVector(equalsList.size, presentList.size)

    // Identifica le posizioni uguali tra il codice e l'input dell'utente
    private def compareToEqual(inputUser: PlayableCode): List[Int] =
      code
        .zip(inputUser)
        .zipWithIndex
        .collect {
          case ((c, u), idx) if c == u => idx
        }
        .toList

    // Identifica le posizioni presenti ma non uguali (senza duplicati)
    private def compareToPresent(inputUser: PlayableCode, equalsList: List[Int]): List[Int] =
      (for
        (c, idx) <- code.zipWithIndex
        if !equalsList.contains(idx) && inputUser.exists(u => u == c && !equalsList.contains(inputUser.indexOf(u)))
      yield idx).distinct.toList

    private def buildVector(equalCount: Int, presentCount: Int): HintCode =
      Vector.fill(equalCount)(HintStone("Red")) ++
        Vector.fill(presentCount)(HintStone("White")) ++
        Vector.fill(code.size - equalCount - presentCount)(HintStone("Empty"))
