package mastermind.utils.prolog

import alice.tuprolog.*
import mastermind.model.entity.Stone
import mastermind.utils.GivenConversion.PrologConversion.given
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

  private def extractTermToString(solveInfo: SolveInfo, s: String): String =
    solveInfo.getTerm(s).toString.replace("'", "")

  private def fromStringToVector[T <: Stone: ClassTag](list: String)(mapper: String => T): Vector[T] =
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

  /** Retrieves the result from a prolog query
    * @param engine
    *   The prolog engine
    * @param functor
    *   The functor to query
    * @param mapper
    *   The function to convert the result to the desired type
    * @param parameters
    *   The parameters to pass to the functor
    * @tparam T
    *   The type of the result
    * @return
    *   The result of the query
    */
  def getResultFromProlog[T <: Stone: ClassTag](
      engine: Term => Iterable[SolveInfo],
      functor: String,
      mapper: String => T,
      parameters: String*
  ): Vector[T] =
    val solveInfo = engine(s"$functor(${parameters.mkString(", ")}, Stones).").head
    val result = extractTermToString(solveInfo, "Stones")
    fromStringToVector(result)(mapper)
