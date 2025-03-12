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

  def apply[T](rows: Int, cols: Int, filling: T): Matrix[T] = (rows, cols, filling) match
    case (rows, cols, filling) if (rows > 0 || cols > 0) && filling != null =>
      MatrixImpl(Vector.fill(rows, cols)(filling))
    case _ => throw new Error("matrix empty or null")

  private case class MatrixImpl[T](override val elements: Vector[Vector[T]]) extends Matrix[T]:
    override def replaceRow(row: Int, vec: Vector[T]): Matrix[T] = copy(elements.updated(row, vec))
    override def replaceCell(row: Int, col: Int, cell: T): Matrix[T] = copy(
      elements.updated(row, elements(row).updated(col, cell))
    )
