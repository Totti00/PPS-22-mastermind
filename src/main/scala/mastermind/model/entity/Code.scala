package mastermind.model.entity

import mastermind.model.entity.Code.{HintStones, PlayableStones}

//TODO capire se posso vedere code come una variabile e fare override del compareTo associato. Sono quasi sicuro si possa fare e che sia più elegante come soluzione
trait Code:
  def compareTo(userInput: PlayableStones): HintStones

object Code:

  private type PlayableStones = Vector[PlayerStoneGrid]
  private type HintStones = Vector[HintStone]

  def apply(size: Int): Code = CodeImpl(Vector.fill(size)(PlayerStoneGrid.random))
  def apply(inputCode: PlayableStones): Code = CodeImpl(inputCode)

  private case class CodeImpl(code: PlayableStones) extends Code:
    override def compareTo(userInput: PlayableStones): HintStones =
      val firstRuleVectors = compareToEqual(userInput)
      val secondRuleVector = compareToPresent(firstRuleVectors._2, userInput)
      val rulesJoinedVector = joinVectors(firstRuleVectors._1, secondRuleVector)
      rulesJoinedVector

    private def compareToEqual(inputUser: PlayableStones): (HintStones, PlayableStones) =
      code.zip(inputUser).zipWithIndex.foldLeft(Vector.empty: HintStones, Vector.empty: PlayableStones) {
        case ((res, res2), ((stoneCode, stoneUser), index))
            if stoneCode.stringRepresentation.equals(stoneUser.stringRepresentation) =>
          (res :+ HintStone("Red"), res2)
        case ((res, res2), ((stoneCode, stoneUser), index)) => (res, res2 :+ stoneCode)
      }

    private def compareToPresent(stonesToCheck: PlayableStones, userInput: PlayableStones): HintStones =
      stonesToCheck.foldLeft(Vector.empty: HintStones) {
        case (res, codeStone) if userInput.distinct.contains(codeStone) => res :+ HintStone("White")
        case (res, codeStone)                                           => res
      }

    private def joinVectors(hintStonesVector: HintStones, hintStonesVector2: HintStones): HintStones =
      val missingPositions = code.size - (hintStonesVector.size + hintStonesVector2.size)
      val joinedVectors: HintStones =
        hintStonesVector ++ hintStonesVector2 ++ Vector.fill(missingPositions)(HintStone("Empty"))
      joinedVectors
