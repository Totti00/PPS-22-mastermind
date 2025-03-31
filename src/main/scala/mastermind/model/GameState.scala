package mastermind.model

sealed trait GameState

/** The game is in progress.
  */
case object InGame extends GameState

/** The player has won the game.
  */
case object PlayerWin extends GameState

/** The player has lost the game.
  */
case object PlayerLose extends GameState
