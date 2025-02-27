package mastermind.model.entity

trait Board:
  def placeGuessAndHints(): Unit
  def getRows: Int
  def getCols: Int

object Board:
  def apply(rows: Int, cols: Int, filling: String = "E", hfilling: String = "E"): Board =
    BoardImpl(Vector.fill(rows * cols)(5), Vector.fill(rows * cols)(5))

  private case class BoardImpl(matrix: Vector[Int], hmatrix: Vector[Int]) extends Board:

    private val rows: Int = 6
    private val cols: Int = 4

    override def placeGuessAndHints(): Unit = println("ciao")

    override def getCols: Int = cols

    override def getRows: Int = rows
