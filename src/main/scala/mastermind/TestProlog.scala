package mastermind

import alice.tuprolog.Term
import mastermind.Scala2P.createEngine
import mastermind.model.entity.{HintStone, HintStones}

object TestProlog extends App:
  val engine = createEngine("/prolog/code.pl")
  /*
  var l = List("HintStones")
  for
    s <- engine(
      Term.createTerm("compareToEqual([green, green, blu, blu], [green, green, red, blu], HintStones).")
    )
    x = Scala2P.extractTermsToListOfStrings(s, l)
    //hintStones1 = mkHintStonesVector(x)
  yield println(hintStones1)
   */
  val l = List("HintStones")
  for
    s <- engine(
      Term.createTerm("compareToEqual([green, green, blu, blu], [green, green, red, red], HintStones).")
    )
    x = Scala2P.extractTermsToListOfStrings(s, l)
  // hintStones2 = mkHintStonesVector(x)
  yield x match
    case stone if stone.exists(_.contains("hintRed"))   => println("red " + stone.count(_.contains("hintRed")))
    case stone if stone.exists(_.contains("hintWhite")) => println("white " + x.head)
    case _                                              => println("nessun match")
