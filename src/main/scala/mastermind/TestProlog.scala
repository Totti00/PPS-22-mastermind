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
    hintStones1 = mkHintStonesVector(x)
  yield println(hintStones1)

  l = List("HintStones")
  for
    s <- engine(
      Term.createTerm("compareToPresent([green, green, blu, blu], [blu, blu, red, red], HintStones).")
    )
    x = Scala2P.extractTermsToListOfStrings(s, l)
    hintStones2 = mkHintStonesVector(x)
  yield println(hintStones2)

  private def mkHintStonesVector(l: List[String]): HintStones = l match
    case List(hintStones) =>
      hintStones
        .stripPrefix("[")
        .stripSuffix("]")
        .split(",")
        .map {
          case "hintRed"   => HintStone.HintRed
          case "hintWhite" => HintStone.HintWhite
        }
        .toVector
    case _ => Vector.empty
