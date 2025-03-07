package mastermind.model.entity

import scala.util.Random

sealed trait Stone:
  def stringRepresentation: String

sealed trait PlayerStoneGrid extends Stone

case object StartCurrentTurn extends PlayerStoneGrid:
  override def stringRepresentation: String = "StartCurrentTurn"
case object Red extends PlayerStoneGrid:
  override def stringRepresentation: String = "Red"
case object Green extends PlayerStoneGrid:
  override def stringRepresentation: String = "Green"
case object Blue extends PlayerStoneGrid:
  override def stringRepresentation: String = "Blue"
case object Yellow extends PlayerStoneGrid:
  override def stringRepresentation: String = "Yellow"
case object White extends PlayerStoneGrid:
  override def stringRepresentation: String = "White"
case object Purple extends PlayerStoneGrid:
  override def stringRepresentation: String = "Purple"
case object Empty extends PlayerStoneGrid:
  override def stringRepresentation: String = "Empty"

object PlayerStoneGrid:
  def apply(stringRepresentation: String): PlayerStoneGrid = stringRepresentation match
    case "Red"              => Red
    case "Green"            => Green
    case "Blue"             => Blue
    case "Yellow"           => Yellow
    case "White"            => White
    case "Purple"           => Purple
    case "Empty"            => Empty
    case "StartCurrentTurn" => StartCurrentTurn
    case _                  => Empty // Default case

  def random: PlayerStoneGrid =
    PlayerStoneGrid(Random.nextInt(6) match
      case 0 => "Red"
      case 1 => "Green"
      case 2 => "Blue"
      case 3 => "Yellow"
      case 4 => "White"
      case 5 => "Purple"
      case _ => "Empty"
    )

sealed trait HintStone extends Stone

case object HintRed extends HintStone:
  override def stringRepresentation = "Red"
case object HintWhite extends HintStone:
  override def stringRepresentation = "White"
case object HintEmpty extends HintStone:
  override def stringRepresentation = "Empty"

object HintStone:
  def apply(stringRepresentation: String): HintStone = stringRepresentation match
    case "Red"   => HintRed
    case "White" => HintWhite
    case "Empty" => HintEmpty
    case _       => HintEmpty // Default case
