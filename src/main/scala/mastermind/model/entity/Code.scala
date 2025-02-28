package mastermind.model.entity

case class Code(code: Vector[Int]):

  def this(size: Int) = this(Vector.fill(size)(5))
  val size: Int = code.size
