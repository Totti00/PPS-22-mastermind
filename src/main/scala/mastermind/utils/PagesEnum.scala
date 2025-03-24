package mastermind.utils

enum PagesEnum:
  /** Menu page
    */
  case Menu

  /** Rules page
    */
  case Rules

  /** Game page
    */
  case Game

  /** Method to get the string representation of the page
    * @return
    *   the string representation of the page
    */
  override def toString: String = this match
    case Menu  => "MenuPage"
    case Rules => "Rules"
    case Game  => "Game"
