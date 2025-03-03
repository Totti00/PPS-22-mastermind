package mastermind.model.singleton

trait GameMode:
  def boardSize: (Int, Int)
  def codeLength: Int
  def name: String

object EasyMode extends GameMode:
  override def boardSize: (Int, Int) = (10, 4)
  override def codeLength: Int = 4
  override def name: String = "Easy"

object MediumMode extends GameMode:
  override def boardSize: (Int, Int) = (8, 5)
  override def codeLength: Int = 5
  override def name: String = "Medium"

object HardMode extends GameMode:
  override def boardSize: (Int, Int) = (6, 5)
  override def codeLength: Int = 5
  override def name: String = "Hard"

object ExtremeMode extends GameMode:
  override def boardSize: (Int, Int) = (6, 6)
  override def codeLength: Int = 6
  override def name: String = "Extreme"
