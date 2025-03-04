package mastermind.model.entity

trait Board:

  def rows: Int
  def cols: Int
  def getPlayableStone(row: Int, cols: Int): Stone
  def getHintStone(row: Int, cols: Int): Stone
  def placeGuessAndHints(
      rowPlayableStone: Vector[PlayableStone],
      rowHintStone: Vector[HintStone],
      updateRow: Int
  ): Board

object Board:
  def apply(
      rows: Int,
      cols: Int,
      playableFilling: PlayableStone = PlayableStone("Empty"),
      hintFilling: HintStone = HintStone("Empty")
  ): Board =
    BoardImpl(Matrix(rows, cols, playableFilling), Matrix(rows, cols, hintFilling))

  private case class BoardImpl(playableMatrix: Matrix[PlayableStone], hintMatrix: Matrix[HintStone]) extends Board:

    override def placeGuessAndHints(
        rowPlayableStone: Vector[PlayableStone],
        rowHintStone: Vector[HintStone],
        updateRow: Int
    ): Board =
      copy(playableMatrix.replaceRow(updateRow, rowPlayableStone), hintMatrix.replaceRow(updateRow, rowHintStone))

    override def rows: Int = playableMatrix.rows
    override def cols: Int = playableMatrix.cols

    override def getPlayableStone(row: Int, cols: Int): PlayableStone = playableMatrix.cell(row, cols)
    override def getHintStone(row: Int, cols: Int): HintStone = hintMatrix.cell(row, cols)
