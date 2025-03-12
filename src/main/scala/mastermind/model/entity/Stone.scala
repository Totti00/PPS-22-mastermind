package mastermind.model.entity

import scala.util.Random

sealed trait Stone

enum PlayerStoneGrid extends Stone:
  case StartCurrentTurn, Empty
  private case Red, Green, Blue, Yellow, White, Purple

  override def toString: String = this match
    case StartCurrentTurn => "StartCurrentTurn"
    case Red              => "Red"
    case Green            => "Green"
    case Blue             => "Blue"
    case Yellow           => "Yellow"
    case White            => "White"
    case Purple           => "Purple"
    case Empty            => "Empty"

object PlayerStoneGrid:
  def random: PlayerStoneGrid =
    val stones = Seq(Red, Green, Blue, Yellow, White, Purple)
    stones(Random.nextInt(stones.length))
  def fromString(stoneColor: String): PlayerStoneGrid =
    values.find(_.toString.equalsIgnoreCase(stoneColor)).getOrElse(Empty)

enum HintStone extends Stone:
  case HintRed, HintWhite, HintEmpty

  override def toString: String = this match
    case HintRed   => "Red"
    case HintWhite => "White"
    case HintEmpty => "Empty"
