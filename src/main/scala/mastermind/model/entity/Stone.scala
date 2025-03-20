package mastermind.model.entity

import alice.tuprolog.Term
import mastermind.Scala2P
import mastermind.Scala2P.createEngine

import scala.util.Random

type PlayableStones = Vector[PlayerStoneGrid]
type HintStones = Vector[HintStone]
val MAX_PERMUTATION = 100

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
  // TODO devono diventare una utils perché viene usata da code e stone. Il secondo no
  private def fromVectorToString(l: Vector[PlayerStoneGrid]): String =
    l.map(_.toString.toLowerCase).mkString("[", ", ", "]")
  private def fromStringToVector(list: String): PlayableStones =
    list.init.tail.split(",").map(PlayerStoneGrid.fromString).toVector

  /** Generates a random set of `PlayableStones` and `colors` based on the provided length.
    *
    * @param codeAndColorLength
    *   The length of the code and possible colors to use.
    * @return
    *   a tuple containing:
    *   - `PlayableStones`: A vector of randomly selected stones, the code.
    *   - `PlayableStones`: A vector of colors used to make the code.
    */
  /*def random(codeAndColorLength: Int): (PlayableStones, PlayableStones) =
    val stones = Vector(Red, Green, Blue, Yellow, White, Purple)
    val colors = Random.shuffle(stones).take(codeAndColorLength)
    val code = Vector.fill(codeAndColorLength)(colors(Random.nextInt(colors.length)))
    (code, colors)*/
  // TODO anche qui ne caso mettere lo stesso metodo di code e cosi poi dopo anche il metodo di code diventa una utils
  def random(codeAndColorLength: Int): (PlayableStones, PlayableStones) =
    val stones = Vector(Red, Green, Blue, Yellow, White, Purple)
    val term = "PlayableStones"

    val permutation100 =
      engine(Term.createTerm(s"permutation(${fromVectorToString(stones)}, ${codeAndColorLength}, PlayableStones)."))
        .take(MAX_PERMUTATION)
        .map(Scala2P.extractTermToString(_, term))
        .toVector
    val momColors = permutation100(Random.nextInt(permutation100.size))
    val colorsToUse = fromStringToVector(momColors)
    val stones2 = stones.take(codeAndColorLength)
    val codes100 =
      engine(
        Term.createTerm(s"codeGenerator(${fromVectorToString(colorsToUse)}, ${codeAndColorLength}, PlayableStones).")
      )
        .take(MAX_PERMUTATION)
        .map(Scala2P.extractTermToString(_, term))
        .toVector
    val momCodeToUse = codes100(Random.nextInt(codes100.size))
    val codeToUse = fromStringToVector(momCodeToUse)
    println("Stone: colorsUsed: " + colorsToUse)
    (codeToUse, colorsToUse)

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
