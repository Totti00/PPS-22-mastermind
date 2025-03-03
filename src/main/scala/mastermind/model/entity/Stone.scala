package mastermind.model.entity

sealed trait Stone:
  def stringRepresentation: String

sealed trait PlayableStone extends Stone

case object Red extends PlayableStone:
  override def stringRepresentation: String = "Red"
case object Green extends PlayableStone:
  override def stringRepresentation: String = "Green"
case object Blue extends PlayableStone:
  override def stringRepresentation: String = "Blue"
case object Yellow extends PlayableStone:
  override def stringRepresentation: String = "Yellow"
case object White extends PlayableStone:
  override def stringRepresentation: String = "White"
case object Purple extends PlayableStone:
  override def stringRepresentation: String = "Purple"
case object Empty extends PlayableStone:
  override def stringRepresentation: String = "Empty"

object PlayableStone:
  def apply(stringRepresentation: String): Stone = stringRepresentation match
    case "Red"    => Red
    case "Green"  => Green
    case "Blue"   => Blue
    case "Yellow" => Yellow
    case "White"  => White
    case "Purple" => Purple
    case "Empty"  => Empty
    case _        => Empty // Default case

sealed trait HintStone extends Stone

case object HintRed extends HintStone:
  override def stringRepresentation = "Red"
case object HintWhite extends HintStone:
  override def stringRepresentation = "White"
case object HintEmpty extends HintStone:
  override def stringRepresentation = "Empty"

object HintStone:
  def apply(stringRepresentation: String): HintStone = stringRepresentation match
    case "R" => HintRed
    case "W" => HintWhite
    case "E" => HintEmpty
    case _   => HintEmpty // Default case
