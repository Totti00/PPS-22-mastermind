package mastermind

import alice.tuprolog.Term
import mastermind.Scala2P.createEngine

object TestProlog extends App:
  val engine = createEngine("/prolog/code.pl")

  //engine("permutation([1,2,3],L)") foreach (println(_))
  // permutation ([1 ,2 ,3] ,[1 ,2 ,3]) ... permutation ([1 ,2 ,3] ,[3 ,2 ,1])

  //val l = List("HintStones")
  val l = List("HintStones")

  for
    s <- engine(
        Term.createTerm("compareToEqual([green, green, blu, blu], [green, green, red, blu], HintStones).")
    )
    x = Scala2P.extractTermsToListOfStrings(s, l)
    //hintStones = mkHintStonesVector(x)
  yield println(x)