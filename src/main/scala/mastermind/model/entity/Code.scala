package mastermind.model.entity

import mastermind.model.entity.Code.{HintStones, PlayableStones}
import mastermind.model.entity.HintStone.{HintEmpty, HintRed, HintWhite}

trait Code:
  /** Takes userInput to perform a comparison finding hintStones
    *
    * @param userInput
    *   Guess of the user
    * @return
    *   Feedback in relation to user input
    */
  def compareTo(userInput: PlayableStones): HintStones

object Code:

  private type PlayableStones = Vector[PlayerStoneGrid]
  private type HintStones = Vector[HintStone]

  def apply(size: Int): Code = CodeImpl(Vector.fill(size)(PlayerStoneGrid.random))

  /** Used for test
    *
    * @param inputCode
    *   Vector of PlayableStone that will be the code
    * @return
    *   A code object
    */
  def apply(inputCode: PlayableStones): Code = CodeImpl(inputCode)

  private case class CodeImpl(code: PlayableStones) extends Code:

    override def compareTo(userInput: PlayableStones): HintStones =
      val firstRuleVectors = compareToEqual(userInput)
      val secondRuleVector = compareToPresent(firstRuleVectors._2, userInput)
      val rulesJoinedVector = joinVectors(firstRuleVectors._1, secondRuleVector)
      rulesJoinedVector

    /** Takes user input to find stones of correct colour in correct position
      *
      * @param inputUser
      *   Guess of the user
      * @return
      *   Vector of HintStones Red and their position
      */
    private def compareToEqual(inputUser: PlayableStones): (HintStones, List[Int]) =
      code.zip(inputUser).zipWithIndex.foldLeft(Vector.empty: HintStones, List.empty: List[Int]) {
        case ((hintStones, indices), ((stoneCode, stoneUser), index))
            if stoneCode.toString.equals(stoneUser.toString) =>
          (hintStones :+ HintRed, indices :+ index)
        case ((hintStones, indices), ((_, _), _)) => (hintStones, indices)
      }

    /** Checks if there are stones of the right colour but positioned wrong
      *
      * @param indicesToDelete
      *   First rule indices that must not be used
      * @param userInput
      *   Guess of the user
      * @return
      *   Vector of HintStones White
      */
    private def compareToPresent(indicesToDelete: List[Int], userInput: PlayableStones): HintStones =
      val codeToCheck = code.zipWithIndex.filter((_, index) => !indicesToDelete.contains(index))
      val userInputToCheck =
        userInput.zipWithIndex.filter((_, index) => !indicesToDelete.contains(index)).map((stone, _) => stone)
      codeToCheck.foldLeft(Vector.empty: HintStones) {
        case (res, (codeStone, index)) if userInputToCheck.contains(codeStone) => res :+ HintWhite
        case (res, (codeStone, index))                                         => res
      }

    /** Create the feedback of hintStones
      *
      * @param hintStonesVector
      *   Vector of the first rule
      * @param hintStonesVector2
      *   Vector of the second rule
      * @return
      *   HintStone feedback consisting of White, Blank and Red
      */
    private def joinVectors(hintStonesVector: HintStones, hintStonesVector2: HintStones): HintStones =
      val missingPositions = code.size - (hintStonesVector.size + hintStonesVector2.size)
      val joinedVectors: HintStones =
        hintStonesVector ++ hintStonesVector2 ++ Vector.fill(missingPositions)(HintEmpty)
      joinedVectors
