package mastermind.model.entity

import mastermind.model.entity.HintStone.*
import mastermind.utils.prolog.Scala2P.*
import mastermind.utils.GivenConversion.PrologConversion.given
import scala.language.implicitConversions

trait Code:
  /** Takes userInput to perform a comparison finding hintStones
    *
    * @param userInput
    *   Guess of the user
    * @return
    *   Feedback in relation to user input
    */
  def compareTo(userInput: PlayableStones): HintStones

  /** Retrieves the colors of the code.
    * @return
    *   the colors of the code represented as `PlayableStones`
    */
  def colors: PlayableStones

object Code:

  def apply(size: Int): Code =
    val (code, colors) = PlayerStone.random(size)
    CodeImpl(code, colors)

  /** Used for test
    *
    * @param codeAndColor
    *   Vector of PlayableStone that will be the code
    * @return
    *   A code object
    */
  def apply(codeAndColor: (PlayableStones, PlayableStones)): Code = CodeImpl(codeAndColor._1, codeAndColor._2)

  private case class CodeImpl(code: PlayableStones, colors: PlayableStones) extends Code:
    private val engine = createEngine("/prolog/code.pl")

    override def compareTo(userInput: PlayableStones): HintStones =
      val hintStonesRed = compareToEqual(userInput)
      val hintStoneWhite = compareToPresent(userInput)
      val rulesJoinedVector = joinVectors(hintStonesRed, hintStoneWhite)
      rulesJoinedVector

    private def compareToPresent(userInput: PlayableStones): HintStones =
      getHintStonesByRule("compareToPresent", code, userInput)

    private def compareToEqual(userInput: PlayableStones): HintStones =
      getHintStonesByRule("compareToEqual", code, userInput)

    private def getHintStonesByRule(functor: String, code: String, userInput: String): HintStones =
      val solveInfo = engine(s"$functor($code, $userInput, HintStones).").head
      val result = extractTermToString(solveInfo, "HintStones")
      fromStringToVector(result) {
        case s if s.toLowerCase == "hintred" => HintRed
        case _                               => HintWhite
      }

    private def joinVectors(hintStonesRed: HintStones, hintStonesWhite: HintStones): HintStones =
      val missingPositions = code.size - (hintStonesRed.size + hintStonesWhite.size)
      val joinedVectors: HintStones =
        hintStonesRed ++ hintStonesWhite ++ Vector.fill(missingPositions)(HintEmpty)
      joinedVectors
