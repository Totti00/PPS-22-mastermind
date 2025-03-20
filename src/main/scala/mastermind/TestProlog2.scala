package mastermind

import alice.tuprolog.{Term, Theory}
import mastermind.Scala2P

object TestProlog2 extends App:
  import mastermind.Scala2P.{*, given}

  val engine = createEngine("/prolog/prova.pl")

  // engine("permutation([1,2,3],L)") foreach (println(_))
  // permutation ([1 ,2 ,3] ,[1 ,2 ,3]) ... permutation ([1 ,2 ,3] ,[3 ,2 ,1])

  // val l = List("HintStones")
  val l = List("X")

  for
    s <- engine(
      Term.createTerm("permutation([blu, w, g],X).")
    )
    x = Scala2P.extractTermsToListOfStrings(s, l)
  yield println(x)
