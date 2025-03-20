package mastermind.model.entity

import alice.tuprolog.Term
import mastermind.Scala2P
import mastermind.Scala2P.createEngine

import scala.util.Random

type PlayableStones = Vector[PlayerStoneGrid]
type HintStones = Vector[HintStone]

sealed trait Stone

enum PlayerStoneGrid extends Stone:
  case StartCurrentTurn, Empty, Win
  private case Red, Green, Blue, Yellow, White, Purple

  override def toString: String = this match
    case StartCurrentTurn => "StartCurrentTurn"
    case Red              => "Red"
    case Green            => "Green"
    case Blue             => "Blue"
    case Yellow           => "Yellow"
    case White            => "White"
    case Purple           => "Purple"
    case Win              => "Win"
    case Empty            => "Empty"

object PlayerStoneGrid:
  private val engine = createEngine("/prolog/prova.pl")
  // TODO deve diventare una utils perché viene usata da code e stone
  private def fromVectorToString(l: Vector[PlayerStoneGrid]): String =
    l.map(_.toString.toLowerCase).mkString("[", ", ", "]")

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
    val colors = Random.shuffle(stones).take(codeAndColorLength)
    val code = Vector.fill(codeAndColorLength)(colors(Random.nextInt(colors.length)))
    (code, colors)

  def random2(codeAndColorLength: Int): Unit =
    val stones = Vector(Red, Green, Blue, Yellow, White, Purple)
    val l = List("PlayableStones")

    val permutation100 =
      engine(Term.createTerm(s"permutation(${fromVectorToString(stones)}, ${codeAndColorLength}, PlayableStones)."))
        .take(100)
        .map(Scala2P.extractTermsToListOfStrings(_, l))
        .toVector
    val colors = permutation100(Random.nextInt(permutation100.size))
    println(colors)
    val stones2 = stones.take(codeAndColorLength)
    val codes100 =
      engine(Term.createTerm(s"codeGenerator(${fromVectorToString(stones2)}, ${codeAndColorLength}, PlayableStones)."))
        .take(100)
        .map(Scala2P.extractTermsToListOfStrings(_, l))
        .toVector
    val codeToUse = codes100(Random.nextInt(codes100.size))
    println(codeToUse)

  /** Converts a string to the corresponding `PlayerStoneGrid` color.
    * @param stoneColor
    *   The color of the stone
    * @return
    *   Gives the requested color or an empty color in case the requested color is not found
    */
  def fromString(stoneColor: String): PlayerStoneGrid =
    values.find(_.toString == stoneColor).getOrElse(Empty)

enum HintStone extends Stone:
  case HintRed, HintWhite, HintEmpty

  override def toString: String = this match
    case HintRed   => "Red"
    case HintWhite => "White"
    case HintEmpty => "Empty"
