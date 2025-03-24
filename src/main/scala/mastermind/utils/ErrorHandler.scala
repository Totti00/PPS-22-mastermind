package mastermind.utils

object ErrorHandler:

  /** Executes a block
    * @param block
    *   the code to execute
    * @tparam T
    *   the type of the result
    * @return
    *   Either[Boolean, T] - Right(result) on success, Left(false) on failure
    */
  def giveMeEither[T](block: => T): Either[Boolean, T] =
    try Right(block)
    catch case ex: Throwable => Left(false)
