package mastermind.utils

enum PagesEnum:
  case Menu, Rules, Game

  override def toString: String = this match
    case Menu  => "MenuPage"
    case Rules => "Rules"
    case Game  => "Game"
