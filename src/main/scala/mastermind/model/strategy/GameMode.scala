package mastermind.model.strategy

trait GameMode:
  def boardSize: (Int, Int)
  def codeAndColorLength: Int
  def name: String

class EasyMode extends GameMode:
  override def boardSize: (Int, Int) = (10, 4)
  override def codeAndColorLength: Int = 4
  override def name: String = "Easy"

class MediumMode extends GameMode:
  override def boardSize: (Int, Int) = (8, 5)
  override def codeAndColorLength: Int = 5
  override def name: String = "Medium"

class HardMode extends GameMode:
  override def boardSize: (Int, Int) = (6, 5)
  override def codeAndColorLength: Int = 5
  override def name: String = "Hard"

class ExtremeMode extends GameMode:
  override def boardSize: (Int, Int) = (6, 6)
  override def codeAndColorLength: Int = 6
  override def name: String = "Extreme"
