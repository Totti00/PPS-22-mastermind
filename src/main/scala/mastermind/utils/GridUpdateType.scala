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
