package mastermind.model.entity

trait Board:

  def rows: Int
  def cols: Int
  def getPlayableStone(row: Int, cols: Int): Stone
  def getHintStone(row: Int, cols: Int): Stone
  def placeGuessAndHints(
      rowPlayableStone: Vector[PlayerStoneGrid],
      rowHintStone: Vector[HintStone],
      updateRow: Int
  ): Board

  def initializeCurrentTurn(currentTurn: Int): Board

object Board:
  def apply(
      rows: Int,
      cols: Int,
      playableFilling: PlayerStoneGrid = PlayerStoneGrid("Empty"),
      hintFilling: HintStone = HintStone("Empty")
  ): Board =
    BoardImpl(Matrix(rows, cols, playableFilling), Matrix(rows, cols, hintFilling))

  private case class BoardImpl(playableMatrix: Matrix[PlayerStoneGrid], hintMatrix: Matrix[HintStone]) extends Board:

    override def placeGuessAndHints(
        rowPlayableStone: Vector[PlayerStoneGrid],
        rowHintStone: Vector[HintStone],
        updateRow: Int
    ): Board =
      copy(playableMatrix.replaceRow(updateRow, rowPlayableStone), hintMatrix.replaceRow(updateRow, rowHintStone))

    override def rows: Int = playableMatrix.rows
    override def cols: Int = playableMatrix.cols

    override def getPlayableStone(row: Int, cols: Int): PlayerStoneGrid = playableMatrix.cell(row, cols)
    override def getHintStone(row: Int, cols: Int): HintStone = hintMatrix.cell(row, cols)

    override def initializeCurrentTurn(currentTurn: Int): Board =
      copy(playableMatrix.replaceRow(currentTurn, Vector.fill(cols)(PlayerStoneGrid("StartCurrentTurn"))), hintMatrix)
