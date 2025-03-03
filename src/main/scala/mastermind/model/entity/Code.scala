package mastermind.model.entity

trait Code:
  def size: Int

object Code:
  def apply(size: Int): Code = CodeImpl(Vector.fill(size)(5))
  
  private case class CodeImpl(code: Vector[Int]) extends Code:
    override def size: Int = code.size
