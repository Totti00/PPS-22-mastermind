package mastermind.utils

import alice.tuprolog.Term
import mastermind.model.entity.PlayableStones

object GivenConversion:

  object PrologConversion:
    given Conversion[PlayableStones, String] = _.map(_.toString.toLowerCase).mkString("[", ", ", "]")
    given Conversion[String, Term] = Term.createTerm(_)
