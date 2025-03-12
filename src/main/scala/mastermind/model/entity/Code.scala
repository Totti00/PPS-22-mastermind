package mastermind.model.entity

import mastermind.model.entity.Code.{HintStones, PlayableStones}
import mastermind.model.entity.HintStone.{HintEmpty, HintRed, HintWhite}

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
      println("codeClass: code: " + code)
      /*
      val firstRuleVectors = compareToEqual2(userInput)
      val secondRuleVector = compareToPresent(firstRuleVectors._2, firstRuleVectors._3)
      val rulesJoinedVector = joinVectors(firstRuleVectors._1, secondRuleVector)
      rulesJoinedVector

       */
      val firstRuleVectors = compareToEqual3(userInput)
      val secondRuleVector = compareToPresent3(firstRuleVectors._2, userInput)
      val rulesJoinedVector = joinVectors(firstRuleVectors._1, secondRuleVector)
      rulesJoinedVector

    /*

    private def compareToEqual(inputUser: PlayableStones): (HintStones, PlayableStones) =
      code.zip(inputUser).zipWithIndex.foldLeft(Vector.empty: HintStones, Vector.empty: PlayableStones) {
        case ((res, res2), ((stoneCode, stoneUser), index))
            if stoneCode.stringRepresentation.equals(stoneUser.stringRepresentation) =>
          (res :+ HintStone("Red"), res2)
        case ((res, res2), ((stoneCode, stoneUser), index)) => (res, res2 :+ stoneCode)
      }

     */

    private def compareToEqual2(inputUser: PlayableStones): (HintStones, PlayableStones, PlayableStones) =
      code
        .zip(inputUser)
        .zipWithIndex
        .foldLeft(Vector.empty: HintStones, Vector.empty: PlayableStones, Vector.empty: PlayableStones) {
          case ((res, res2, res3), ((stoneCode, stoneUser), index)) if stoneCode.toString.equals(stoneUser.toString) =>
            (res :+ HintRed, res2, res3)
          case ((res, res2, res3), ((stoneCode, stoneUser), index)) => (res, res2 :+ stoneCode, res3 :+ stoneUser)
        }

    private def compareToEqual3(inputUser: PlayableStones): (HintStones, List[Int]) =
      code.zip(inputUser).zipWithIndex.foldLeft(Vector.empty: HintStones, List.empty: List[Int]) {
        case ((res, res1), ((stoneCode, stoneUser), index)) if stoneCode.toString.equals(stoneUser.toString) =>
          (res :+ HintRed, res1 :+ index)
        case ((res, res1), ((stoneCode, stoneUser), index)) => (res, res1)
      }

    private def compareToPresent3(indicesToDelete: List[Int], userInput: PlayableStones): HintStones =
      val codeToCheck = code.zipWithIndex.filter((_, index) => !indicesToDelete.contains(index))
      val userInputToCheck =
        userInput.zipWithIndex.filter((_, index) => !indicesToDelete.contains(index)).map((stone, _) => stone)

      codeToCheck.foldLeft(Vector.empty: HintStones) {
        case (res, (codeStone, index)) if userInputToCheck.contains(codeStone) => res :+ HintWhite
        case (res, (codeStone, index))                                         => res
      }

    private def compareToPresent(stonesToCheck: PlayableStones, userInput: PlayableStones): HintStones =
      stonesToCheck.foldLeft(Vector.empty: HintStones) {
        case (res, codeStone) if userInput.distinct.contains(codeStone) => res :+ HintWhite
        case (res, codeStone)                                           => res
      }

    private def joinVectors(hintStonesVector: HintStones, hintStonesVector2: HintStones): HintStones =
      val missingPositions = code.size - (hintStonesVector.size + hintStonesVector2.size)
      val joinedVectors: HintStones =
        hintStonesVector ++ hintStonesVector2 ++ Vector.fill(missingPositions)(HintEmpty)
      joinedVectors
