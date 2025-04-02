package mastermind.model.entity

import mastermind.utils.prolog.Scala2P.*
import mastermind.utils.GivenConversion.PrologConversion.given
import scala.util.Random
import scala.language.implicitConversions

type PlayableStones = Vector[PlayerStone]
type HintStones = Vector[HintStone]
val MAX_PERMUTATION = 100

sealed trait Stone

enum PlayerStone extends Stone:
  case Playable, Empty, Win
  private case Red, Green, Blue, Yellow, White, Purple

  /** Method to get the string representation of the page
    * @return
    *   The string representation of the page
    */
  override def toString: String = this match
    case Playable => "Playable"
    case Red      => "Red"
    case Green    => "Green"
    case Blue     => "Blue"
    case Yellow   => "Yellow"
    case White    => "White"
    case Purple   => "Purple"
    case Win      => "Win"
    case Empty    => "Empty"

object PlayerStone:
  private val engine = createEngine("/prolog/stone.pl")

  /** Generates a random set of `PlayableStones` and `colors` based on the provided length.
    *
    * @param codeAndColorLength
    *   The length of the code and possible colors to use.
    * @return
    *   a tuple containing:
    *   - `PlayableStones`: A vector of randomly selected stones, the code.
    *   - `PlayableStones`: A vector of colors used to make the code.
    */
  def random(codeAndColorLength: Int): (PlayableStones, PlayableStones) =
    val stones = Vector(Red, Green, Blue, Yellow, White, Purple)
    val colors = getResultFromProlog(engine, "colors", mapper, stones, codeAndColorLength.toString)
    val code = getResultFromProlog(engine, "codeGenerator", mapper, colors, codeAndColorLength.toString)
    (code, colors)

  private def mapper = PlayerStone.fromString

  /** Converts a string to the corresponding `PlayerStoneGrid` color.
    * @param stoneColor
    *   The color of the stone
    * @return
    *   Gives the requested color or an empty color in case the requested color is not found
    */
  def fromString(stoneColor: String): PlayerStone =
    values.find(_.toString == stoneColor.toLowerCase.capitalize).getOrElse(Empty)

enum HintStone extends Stone:
  case HintRed, HintWhite, HintEmpty

  override def toString: String = this match
    case HintRed   => "Red"
    case HintWhite => "White"
    case HintEmpty => "Empty"
