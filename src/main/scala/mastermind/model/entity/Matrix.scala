package mastermind.model.entity

trait Matrix[T]:
  def elements: Vector[Vector[T]]
  def rows: Int = elements.size
  def cols: Int = if elements.nonEmpty then elements.head.size else 0
  def cell(row: Int, col: Int): T = elements(row)(col)
  def row(row: Int): Seq[T] = elements(row)
  def replaceCell(row: Int, col: Int, cell: T): Matrix[T]
  def replaceRow(row: Int, vec: Vector[T]): Matrix[T]

object Matrix:

  def apply[T](rows: Int, cols: Int, filling: T): Matrix[T] =
    MatrixImpl(Vector.fill(rows, cols)(filling))

  private case class MatrixImpl[T](override val elements: Vector[Vector[T]]) extends Matrix[T]:
    assert(elements != null && elements.nonEmpty, "matrix empty or null")
    override def replaceRow(row: Int, vec: Vector[T]): Matrix[T] = copy(elements.updated(row, vec))
    override def replaceCell(row: Int, col: Int, cell: T): Matrix[T] = copy(
      elements.updated(row, elements(row).updated(col, cell))
    )
