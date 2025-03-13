package mastermind.model.entity

import mastermind.model.entity.HintStone.{HintEmpty, HintRed}
import mastermind.model.entity.PlayerStoneGrid.{Empty, StartCurrentTurn, Win}

trait Board:

  def rows: Int
  def cols: Int

  /** @param row
    * @param cols
    * @return
    *   Gives the PlayableStoneGrid requested
    */
  def getPlayableStone(row: Int, cols: Int): PlayerStoneGrid

  /** @param row
    * @param cols
    * @return
    *   Gives the HintStone requested
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
    *   The new board of the game
    */
  def placeGuessAndHints(
      rowPlayableStone: Vector[PlayerStoneGrid],
      rowHintStone: Vector[HintStone],
      updateRow: Int
  ): Board

  /** Creates the board of gold and red stones if the player win
    *
    * @return
    *   The new board of the game
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
      copy(playableMatrix.replaceRow(currentTurn, Vector.fill(cols)(StartCurrentTurn)), hintMatrix)

    override def winBoard(): Board =
      copy(Matrix(rows, cols, Win), Matrix(rows, cols, HintRed))
