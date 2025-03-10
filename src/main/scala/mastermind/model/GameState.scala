package mastermind.model

/** Represents the current state of the game.
  */
sealed trait GameState

object GameState:
  /** The game is in progress.
    */
  case object InGame extends GameState

  /** The player has won the game.
    */
  case object PlayerWin extends GameState

  /** The player has lost the game.
    */
  case object PlayerLose extends GameState

  /** The player has quit the game.
    */
  case object MainMenu extends GameState
