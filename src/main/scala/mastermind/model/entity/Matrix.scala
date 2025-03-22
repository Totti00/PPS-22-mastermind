package mastermind.model.entity

import mastermind.utils.throwableToLeft

trait Matrix[T]:
  /** Retrieves the elements of the matrix.
    * @return
    *   the matrix elements as a `Vector` of `Vector`s, where each inner `Vector` represents a row of the matrix.
    */
  def elements: Vector[Vector[T]]

  /** Retrieves the number of rows in the matrix.
    * @return
    *   The rows of the matrix
    */
  def rows: Int = elements.size

  /** Retrieves the number of columns in the matrix.
    * @return
    *   The columns of the matrix
    */
  def cols: Int = elements.head.size

  /** Retrieves the element at a specific row and column in the matrix.
    * @param row
    *   The row index
    * @param col
    *   The column index
    * @return
    *   The element of the matrix requested
    */
  def cell(row: Int, col: Int): T = elements(row)(col)

  /** Retrieves the entire row
    * @param row
    *   The row index
    * @return
    *   The row of matrix requested
    */
  def row(row: Int): Vector[T] = elements(row)

  /** Replaces an element at a specific row and column in the matrix with a new element.
    *
    * @param row
    *   The row index
    * @param col
    *   The column index
    * @param cell
    *   The element to be inserted in the matrix
    * @return
    *   New matrix with the element replaced by the new one
    */
  def replaceCell(row: Int, col: Int, cell: T): Matrix[T]

  /** Replace an entire row of the matrix
    *
    * @param row
    *   The row index
    * @param vec
    *   The new row to be inserted in the matrix
    * @return
    *   New matrix with the row replaced by the new one
    */
  def replaceRow(row: Int, vec: Vector[T]): Matrix[T]

object Matrix:

  def apply[T](rows: Int, cols: Int, filling: T): Either[Throwable, Matrix[T]] =
    throwableToLeft {
      require(rows >= 0 && cols >= 0 && filling != null, "Matrix empty or null")
      MatrixImpl(Vector.fill(rows, cols)(filling))
    }

  private case class MatrixImpl[T](override val elements: Vector[Vector[T]]) extends Matrix[T]:
    override def replaceRow(row: Int, vec: Vector[T]): Matrix[T] = copy(elements.updated(row, vec))
    override def replaceCell(row: Int, col: Int, cell: T): Matrix[T] = copy(
      elements.updated(row, elements(row).updated(col, cell))
    )
