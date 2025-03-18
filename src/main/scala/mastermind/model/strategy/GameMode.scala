package mastermind.model.strategy

trait GameMode:
  def boardSize: (Int, Int)
  def codeAndColorLength: Int
  def name: String

/** Easy game mode with a 10x4 board and a code length of 4.
  */
class EasyMode extends GameMode:
  override def boardSize: (Int, Int) = (10, 4)
  override def codeAndColorLength: Int = 4
  override def name: String = "Easy"

/** Medium game mode with an 8x5 board and a code length of 5.
  */
class MediumMode extends GameMode:
  override def boardSize: (Int, Int) = (8, 5)
  override def codeAndColorLength: Int = 5
  override def name: String = "Medium"

/** Hard game mode with a 6x5 board and a code length of 5.
  */
class HardMode extends GameMode:
  override def boardSize: (Int, Int) = (6, 5)
  override def codeAndColorLength: Int = 5
  override def name: String = "Hard"

/** Extreme game mode with a 6x6 board and a code length of 6.
  */
class ExtremeMode extends GameMode:
  override def boardSize: (Int, Int) = (6, 6)
  override def codeAndColorLength: Int = 6
  override def name: String = "Extreme"
