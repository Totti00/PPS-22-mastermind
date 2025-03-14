package mastermind.model.strategy

trait GameMode:
  def boardSize: (Int, Int)
  def codeLength: Int
  def name: String

class EasyMode extends GameMode:
  override def boardSize: (Int, Int) = (10, 4)
  override def codeLength: Int = 4
  override def name: String = "Easy"

class MediumMode extends GameMode:
  override def boardSize: (Int, Int) = (8, 5)
  override def codeLength: Int = 5
  override def name: String = "Medium"

class HardMode extends GameMode:
  override def boardSize: (Int, Int) = (6, 5)
  override def codeLength: Int = 5
  override def name: String = "Hard"

class ExtremeMode extends GameMode:
  override def boardSize: (Int, Int) = (6, 6)
  override def codeLength: Int = 6
  override def name: String = "Extreme"
