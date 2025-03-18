package mastermind.model.strategy

trait GameMode:
  /**
   * The size of the board
   * @return
   *    A tuple (rows, cols) representing the dimensions of the board
   */
  def boardSize: (Int, Int)

  /**
   * The length of the secret code, which defines how many colors are in the code
   * @return
   *    The number of stones and the colors to use in the code
   */
  def codeAndColorLength: Int

  /**
   * The name of the game mode
   * @return
   *    A string representing the name of the game mode
   */
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
