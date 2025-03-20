package mastermind

import alice.tuprolog.Term
import mastermind.Scala2P.createEngine
import mastermind.model.entity.{HintStone, HintStones}

object TestProlog extends App:
  val engine = createEngine("/prolog/code.pl")

  var l = List("HintStones")
  for
    s <- engine(
      Term.createTerm("compareToEqual([green, green, blu, blu], [green, green, red, blu], HintStones).")
    )
    x = Scala2P.extractTermsToListOfStrings(s, l)
    hintStones = mkHintStonesVector(x)
  yield println(hintStones)

  private def mkHintStonesVector(l: List[String]): HintStones = l match
    case List(hintStones) => hintStones.split(",").map(_ => HintStone.HintRed).toVector
    case _                => Vector.empty

  l = List("HintStones")
  for
    s <- engine(
      Term.createTerm("compareToPresent([green, green, blu, blu], [blu, blu, red, red], HintStones).")
    )
    x = Scala2P.extractTermsToListOfStrings(s, l)
    hintStones = mkHintStonesVector(x)
  yield println(hintStones)
