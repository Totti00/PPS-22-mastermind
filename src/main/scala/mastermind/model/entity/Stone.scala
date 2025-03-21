package mastermind.model.entity

import mastermind.utils.prolog.Scala2P.*
import mastermind.utils.GivenConversion.PrologConversion.given
import scala.util.Random
import scala.language.implicitConversions

type PlayableStones = Vector[PlayerStoneGrid]
type HintStones = Vector[HintStone]
val MAX_PERMUTATION = 100

sealed trait Stone

enum PlayerStoneGrid extends Stone:
  case Playable, Empty, Win
  private case Red, Green, Blue, Yellow, White, Purple

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

object PlayerStoneGrid:
  private val engine = createEngine("/prolog/stone.pl")

  private def obtainVectorPlayable(stones: String, functor: String, codeAndColorLength: Int): Vector[String] =
    engine(s"$functor($stones, $codeAndColorLength, PlayableStones).")
      .take(MAX_PERMUTATION)
      .map(extractTermToString(_, "PlayableStones"))
      .toVector

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
    val colors = randomHelper(stones, "permutation", codeAndColorLength)
    val code = randomHelper(colors, "codeGenerator", codeAndColorLength)

    println("Stone: code: " + code)
    (code, colors)

  private def randomHelper(stones: PlayableStones, functor: String, codeAndColorLength: Int): PlayableStones =
    val permutations = obtainVectorPlayable(stones, functor, codeAndColorLength)
    val drawOne = permutations(Random.nextInt(permutations.size))
    fromStringToVector(drawOne)(mapper)

  private def mapper = PlayerStoneGrid.fromString

  /** Converts a string to the corresponding `PlayerStoneGrid` color.
    * @param stoneColor
    *   The color of the stone
    * @return
    *   Gives the requested color or an empty color in case the requested color is not found
    */
  def fromString(stoneColor: String): PlayerStoneGrid =
    values.find(_.toString == stoneColor.toLowerCase.capitalize).getOrElse(Empty)

enum HintStone extends Stone:
  case HintRed, HintWhite, HintEmpty

  override def toString: String = this match
    case HintRed   => "Red"
    case HintWhite => "White"
    case HintEmpty => "Empty"
