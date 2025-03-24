package mastermind.utils.prolog

import alice.tuprolog.*
import mastermind.model.entity.Stone
import scala.reflect.ClassTag

object Scala2P:
  /** The prolog engine for solving goals from the given theory.
    *
    * @param theory
    *   the theory used for solving goal
    * @return
    *   a [[Iterable]] of [[SolveInfo]]
    */
  def createEngine(theory: String): Term => Iterable[SolveInfo] =
    prologEngine(
      Theory.parseLazilyWithStandardOperators(
        getClass.getResourceAsStream(theory)
      )
    )

  /** Extracts a Term and converts it to a String
    *
    * @param solveInfo
    *   The solve info
    * @param s
    *   The term to extract
    * @return
    *   The extracted term
    */
  def extractTermToString(solveInfo: SolveInfo, s: String): String =
    solveInfo.getTerm(s).toString.replace("'", "")

  /** Converts a string to a vector
    * @param list
    *   The string representing the list, formatted as `"[elem1, elem2, elem3, ...]"`
    * @param mapper
    *   A function to convert each string element of the list to an instance of type `T`
    * @tparam T
    *   The type of the elements in the resulting vector. It must be a subtype of `Stone`.
    * @return
    *   A Vector containing the elements of the list converted by the "mapper" function.
    */
  def fromStringToVector[T <: Stone: ClassTag](list: String)(mapper: String => T): Vector[T] =
    if list == "[]" then Vector.empty
    else list.init.tail.split(",").map(mapper).toVector

  private def prologEngine(theory: Theory): Term => Iterable[SolveInfo] =
    val engine = Prolog()
    engine.setTheory(theory)
    goal =>
      new Iterable[SolveInfo]:
        override def iterator: Iterator[SolveInfo] = new Iterator[SolveInfo]:
          var solution: Option[SolveInfo] = Some(engine.solve(goal))

          override def hasNext: Boolean =
            solution.fold(false)(f => f.hasOpenAlternatives || f.isSuccess)

          override def next(): SolveInfo =
            try solution.get
            finally
              solution =
                if solution.get.hasOpenAlternatives then Some(engine.solveNext())
                else None
