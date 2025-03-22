package mastermind.utils

sealed trait GridUpdateType

/** Represents the initialization of the game grid.
  */
case object Initialize extends GridUpdateType

/** Represents the update of the hint stones in the game grid.
  */
case object UpdateHint extends GridUpdateType

/** Represents the update of the playable stones in the game grid.
  */
case object UpdatePlayable extends GridUpdateType
//TODO da spostare
def throwableToLeft[T](block: => T): Either[Throwable, T] =
  try Right(block)
  catch case ex: Throwable => Left(ex)

def extractRightLeft[T](either: Either[Throwable, T]): T = either match
  case Right(result) => result
  case Left(ex)      => throw ex
