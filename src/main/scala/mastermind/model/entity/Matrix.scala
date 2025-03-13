package mastermind.model.entity

trait Matrix[T]:
  /** @return
    *   The matrix composed by the elements
    */
  def elements: Vector[Vector[T]]

  /** @return
    *   The rows of the matrix
    */
  def rows: Int = elements.size

  /** @return
    *   The columns of the matrix
    */
  def cols: Int = elements.head.size

  /** @param row
    * @param col
    * @return
    *   The element of the matrix requested
    */
  def cell(row: Int, col: Int): T = elements(row)(col)

  /** @param row
    * @return
    *   The row of matrix requested
    */
  def row(row: Int): Seq[T] = elements(row)

  /** Replace of an element in the matrix
    *
    * @param row
    * @param col
    * @param cell
    *   The element to be inserted in the matrix
    * @return
    *   New matrix with the element replaced by the new one
    */
  def replaceCell(row: Int, col: Int, cell: T): Matrix[T]

  /** Replace an entire row of the matrix
    *
    * @param row
    * @param vec
    *   The row to be inserted in the matrix
    * @return
    *   New matrix with the row replaced by the new one
    */
  def replaceRow(row: Int, vec: Vector[T]): Matrix[T]

object Matrix:

  def apply[T](rows: Int, cols: Int, filling: T): Matrix[T] = (rows, cols, filling) match
    case (rows, cols, filling) if (rows > 0 || cols > 0) && filling != null =>
      MatrixImpl(Vector.fill(rows, cols)(filling))
    case _ => throw new Error("matrix empty or null")

  private case class MatrixImpl[T](override val elements: Vector[Vector[T]]) extends Matrix[T]:
    override def replaceRow(row: Int, vec: Vector[T]): Matrix[T] = copy(elements.updated(row, vec))
    override def replaceCell(row: Int, col: Int, cell: T): Matrix[T] = copy(
      elements.updated(row, elements(row).updated(col, cell))
    )
