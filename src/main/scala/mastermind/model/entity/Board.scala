package mastermind.model.entity

import mastermind.model.entity.HintStone.{HintEmpty, HintRed}
import mastermind.model.entity.PlayerStoneGrid.{Empty, StartCurrentTurn, Win}

trait Board:
  /** The number of rows in the board.
    * @return
    *   The number of rows in the board.
    */
  def rows: Int

  /** The number of cols in the board.
    * @return
    *   The number of cols in the board.
    */
  def cols: Int

  /** Retrieves the playable stone at a given position on the board.
    * @param row
    *   row index of matrix
    * @param cols
    *   cols index of the matrix
    * @return
    *   the playableGridStone at the specified row and column.
    */
  def getPlayableStone(row: Int, cols: Int): PlayerStoneGrid

  /** @param row
    *   row index of matrix
    * @param cols
    *   cols index of the matrix
    * @return
    *   the hintStone at the specified row and column.
    */
  def getHintStone(row: Int, cols: Int): HintStone

  /** Create a new board with the user's guess and the feedback associated with it.
    *
    * @param rowPlayableStone
    *   Guess of the user
    * @param rowHintStone
    *   Feedback associated to the guess
    * @param updateRow
    *   The row that need to be updated
    * @return
    *   a new board with the updated row.
    */
  def placeGuessAndHints(
      rowPlayableStone: PlayableStones,
      rowHintStone: HintStones,
      updateRow: Int
  ): Board

  /** Creates a new board where the stones are set to represent a winning state. This method sets all the stones on the
    * board to `Win` and the hint stones to `HintRed`.
    * @return
    *   a new board with the winning state.
    */
  def winBoard(): Board

  /** Creates a visualisation indicating the stones playable in the turn, using a colour.
    *
    * @param currentTurn
    *   The turn that need to be initialized
    * @return
    *   The new board of the game
    */
  def initializeCurrentTurn(currentTurn: Int): Board

object Board:
  def apply(
      rows: Int,
      cols: Int,
      playableFilling: PlayerStoneGrid = Empty,
      hintFilling: HintStone = HintEmpty
  ): Board =
    BoardImpl(Matrix(rows, cols, playableFilling), Matrix(rows, cols, hintFilling))

  private case class BoardImpl(playableMatrix: Matrix[PlayerStoneGrid], hintMatrix: Matrix[HintStone]) extends Board:

    override def placeGuessAndHints(
        rowPlayableStone: PlayableStones,
        rowHintStone: HintStones,
        updateRow: Int
    ): Board =
      copy(playableMatrix.replaceRow(updateRow, rowPlayableStone), hintMatrix.replaceRow(updateRow, rowHintStone))

    override def rows: Int = playableMatrix.rows
    override def cols: Int = playableMatrix.cols

    override def getPlayableStone(row: Int, cols: Int): PlayerStoneGrid = playableMatrix.cell(row, cols)
    override def getHintStone(row: Int, cols: Int): HintStone = hintMatrix.cell(row, cols)

    override def initializeCurrentTurn(currentTurn: Int): Board =
      copy(playableMatrix.replaceRow(currentTurn, Vector.fill(cols)(StartCurrentTurn)), hintMatrix)

    override def winBoard(): Board =
      copy(Matrix(rows, cols, Win), Matrix(rows, cols, HintRed))
