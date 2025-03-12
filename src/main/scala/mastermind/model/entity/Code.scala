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
      val firstRuleVector = compareToEqual(userInput)
      val secondRuleVector = compareToPresent(firstRuleVector._2)
      firstRuleVector._1
      // val rulesJoinedVector = joinVectors(firstRuleVector, secondRuleVector)
      // rulesJoinedVector

    private def compareToEqual(inputUser: PlayableStones): (HintStones, PlayableStones) =
      code.zip(inputUser).zipWithIndex.foldLeft(Vector.empty: HintStones, Vector.empty: PlayableStones) {
        case ((res, res2), ((stoneCode, stoneUser), index))
            if stoneCode.stringRepresentation.equals(stoneUser.stringRepresentation) =>
          (res :+ HintStone("Red"), res2)
        case ((res, res2), ((stoneCode, stoneUser), index)) => (res, res2 :+ stoneCode)
      }

    private def compareToPresent(inputFirstRule: PlayableStones): HintStones =
      inputFirstRule.zipWithIndex.foldLeft(Vector.empty: HintStones) {
        case (res, (stoneUser, index))
            if code.contains(stoneUser) && !code(index).stringRepresentation.equals(stoneUser.stringRepresentation) =>
          res :+ HintStone("White")
        case (res, (stoneUser, index)) => res :+ HintStone("Empty")
      }

    private def joinVectors(hintStonesVector: HintStones, hintStonesVector2: HintStones): HintStones =
      hintStonesVector.zip(hintStonesVector2).foldLeft(Vector.empty: HintStones) {
        case (res, (hintStone, hintStone2)) if hintStone.stringRepresentation.equals("Empty") => res :+ hintStone2
        case (res, (hintStone, hintStone2))                                                   => res :+ hintStone
      }
