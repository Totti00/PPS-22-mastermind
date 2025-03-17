package mastermind.utils

sealed trait GridUpdateType

case object Initialize extends GridUpdateType
case object UpdateHint extends GridUpdateType
case object UpdatePlayable extends GridUpdateType
