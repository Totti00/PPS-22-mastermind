package mastermind.model.entity

import alice.tuprolog.Term
import mastermind.Scala2P
import mastermind.Scala2P.createEngine
import mastermind.model.entity.HintStone.{HintEmpty, HintRed, HintWhite}

trait Code:
  /** Takes userInput to perform a comparison finding hintStones
    *
    * @param userInput
    *   Guess of the user
    * @return
    *   Feedback in relation to user input
    */
  def compareTo(userInput: PlayableStones): HintStones

  /** Retrieves the colors of the code.
    * @return
    *   the colors of the code represented as `PlayableStones`
    */
  def colors: PlayableStones

object Code:

  def apply(size: Int): Code = CodeImpl(PlayerStoneGrid.random(size))

  /** Used for test
    *
    * @param codeAndColor
    *   Vector of PlayableStone that will be the code
    * @return
    *   A code object
    */
  def apply(codeAndColor: (PlayableStones, PlayableStones)): Code = CodeImpl(codeAndColor)

  private case class CodeImpl(codeAndColor: (PlayableStones, PlayableStones)) extends Code:

    private val code = codeAndColor._1

    private val engine = createEngine("/prolog/code.pl")

    override def colors: PlayableStones =
      codeAndColor._2

    override def compareTo(userInput: PlayableStones): HintStones =
      println("Code -> " + fromVectorToString(code))
      println("UserInput -> " + fromVectorToString(userInput))

      val hintStonesRed = compareToEqual(userInput)
      println("HintStoneRed -> " + hintStonesRed)
      val secondRuleVector = compareToPresent(userInput)
      println("HintWhiteRed -> " + secondRuleVector)
      val rulesJoinedVector = joinVectors(hintStonesRed, secondRuleVector)
      rulesJoinedVector

    private def fromVectorToString(l: Vector[PlayerStoneGrid]): String =
      l.map(_.toString.toLowerCase).mkString("[", ", ", "]")

    /** Takes user input to find stones of correct colour in correct position
      *
      * @param inputUser
      *   Guess of the user
      * @return
      *   Vector of HintStones Red and their position
      */
    /*private def compareToEqual(inputUser: PlayableStones): HintStones =
      val l = List("HintStones")
      // Esegui engine e ottieni il primo elemento dell'Iterable (se esiste)
      val hintStones1 = engine(
        Term.createTerm(s"compareToEqual(${fromVectorToString(code)}, ${fromVectorToString(inputUser)}, HintStones).")
      ).flatMap { s =>
        val x = Scala2P.extractTermsToListOfStrings(s, l)
        // Usa mkHintStonesVector per ottenere il HintStones
        Some(stringsToVector(x))
        //Some(mkHintStonesVector(x))
      }

      // Se engine restituisce un Iterable non vuoto, prendi il primo elemento
      hintStones1.headOption.getOrElse(Vector.empty)*/

    /** Checks if there are stones of the right colour but positioned wrong
      *
      * @param indicesToDelete
      *   First rule indices that must not be used
      * @param userInput
      *   Guess of the user
      * @return
      *   Vector of HintStones White
      */
    /*private def compareToPresent(userInput: PlayableStones): HintStones =
      val r = List("HintStones")
      // Usa il `map` per trasformare i risultati direttamente
      val hintStones2 = engine(
        Term.createTerm(s"compareToPresent(${fromVectorToString(code)}, ${fromVectorToString(userInput)}, HintStones).")
      ).flatMap { s =>
        val x = Scala2P.extractTermsToListOfStrings(s, r)
        // Usa mkHintStonesVector per ottenere il HintStones
        println("compareToPResent: "+stringsToVector(x)+" "+x)
        Some(stringsToVector(x))
        //Some(mkHintStonesVector(x))
      }
      hintStones2.headOption.getOrElse(Vector.empty)*/

    // TODO risolto i problemi dell'altro e penso che si possano ottimizzare ancora di più. Ci ho speso poco tempo
    // ora ho lasciato sia la versione con una funzione che divisa. Domani insieme ci guardo con jackie chansss
    private def compareToPresent(userInput: PlayableStones): HintStones =
      /*engine(
        Term.createTerm(s"compareToPresent(${fromVectorToString(code)}, ${fromVectorToString(userInput)}, HintStones).")
      ).map { s =>
        val term = Scala2P.extractTermsToListOfStrings(s, List("HintStones"))
        Some(stringsToVector(term))
      }.head.getOrElse(Vector.empty)*/
      getHintStonesByRule("compareToPresent", userInput)

    private def compareToEqual(inputUser: PlayableStones): HintStones =
      /*engine(
        Term.createTerm(s"compareToEqual(${fromVectorToString(code)}, ${fromVectorToString(inputUser)}, HintStones).")
      ).map { s =>
        val x = Scala2P.extractTermsToListOfStrings(s, List("HintStones"))
        Some(stringsToVector(x))
      }.head.getOrElse(Vector.empty)*/
      getHintStonesByRule("compareToEqual", inputUser)

    private def getHintStonesByRule(rule: String, inputUser: PlayableStones): HintStones =
      engine(
        Term.createTerm(s"${rule}(${fromVectorToString(code)}, ${fromVectorToString(inputUser)}, HintStones).")
      ).map { s =>
        val x = Scala2P.extractTermsToListOfStrings(s, List("HintStones"))
        Some(stringsToVector(x))
      }.head
        .getOrElse(Vector.empty)

    /** Create the feedback of hintStones
      *
      * @param hintStonesVector
      *   Vector of the first rule
      * @param hintStonesVector2
      *   Vector of the second rule
      * @return
      *   HintStone feedback consisting of White, Blank and Red
      */
    private def joinVectors(hintStonesVector: HintStones, hintStonesVector2: HintStones): HintStones =
      val missingPositions = code.size - (hintStonesVector.size + hintStonesVector2.size)
      val joinedVectors: HintStones =
        hintStonesVector ++ hintStonesVector2 ++ Vector.fill(missingPositions)(HintEmpty)
      joinedVectors
  /*
    private def mkHintStonesVector(l: List[String]): HintStones = l match
      case List(hintStones) =>
        // Mappa direttamente i valori e crea il Vector
        hintStones
          .stripPrefix("[")
          .stripSuffix("]")
          .split(",")
          .map {
            case "hintRed"   => HintRed
            case "hintWhite" => HintWhite
            case _           => HintEmpty // Aggiungi una default case per gestire altri valori. è da togliere perché senno ha un vettore con empty e allora hai una posizione in più
          }
          .toVector
      case _ => Vector.empty
   */
  private def stringsToVector(l: List[String]): HintStones = l match
    case stones if stones.exists(_.contains("hintRed")) => Vector.fill("hintRed".r.findAllIn(stones.head).size)(HintRed)
    case stones if stones.exists(_.contains("hintWhite")) =>
      Vector.fill("hintWhite".r.findAllIn(stones.head).size)(HintWhite)
    case _ => Vector.empty

/*override def colors: PlayableStones =
  codeAndColor._2

override def compareTo(userInput: PlayableStones): HintStones =
  val firstRuleVectors = compareToEqual(userInput)
  val secondRuleVector = compareToPresent(firstRuleVectors._2, userInput)
  val rulesJoinedVector = joinVectors(firstRuleVectors._1, secondRuleVector)
  rulesJoinedVector*/

/** Takes user input to find stones of correct colour in correct position
  *
  * @param inputUser
  *   Guess of the user
  * @return
  *   Vector of HintStones Red and their position
  */
/*private def compareToEqual(inputUser: PlayableStones): (HintStones, List[Int]) =
    code.zip(inputUser).zipWithIndex.foldLeft(Vector.empty: HintStones, List.empty: List[Int]) {
      case ((hintStones, indices), ((stoneCode, stoneUser), index)) if stoneCode == stoneUser =>
        (hintStones :+ HintRed, indices :+ index)
      case ((hintStones, indices), ((_, _), _)) => (hintStones, indices)
    }*/

/** Checks if there are stones of the right colour but positioned wrong
  *
  * @param indicesToDelete
  *   First rule indices that must not be used
  * @param userInput
  *   Guess of the user
  * @return
  *   Vector of HintStones White
  */
/*private def compareToPresent(indicesToDelete: List[Int], userInput: PlayableStones): HintStones =
   val codeToCheck = code.zipWithIndex.filter((_, index) => !indicesToDelete.contains(index))
   val userInputToCheck =
     userInput.zipWithIndex.filter((_, index) => !indicesToDelete.contains(index)).map((stone, _) => stone)
   codeToCheck.foldLeft(Vector.empty: HintStones) {
     case (res, (codeStone, index)) if userInputToCheck.contains(codeStone) => res :+ HintWhite
     case (res, (codeStone, index))                                         => res
   }*/

/** Create the feedback of hintStones
  *
  * @param hintStonesVector
  *   Vector of the first rule
  * @param hintStonesVector2
  *   Vector of the second rule
  * @return
  *   HintStone feedback consisting of White, Blank and Red
  */
/*private def joinVectors(hintStonesVector: HintStones, hintStonesVector2: HintStones): HintStones =
    val missingPositions = code.size - (hintStonesVector.size + hintStonesVector2.size)
    val joinedVectors: HintStones =
      hintStonesVector ++ hintStonesVector2 ++ Vector.fill(missingPositions)(HintEmpty)
    joinedVectors


  private def mkHintStonesVector(l: List[String]): HintStones = l match
    case List(hintStones) => hintStones.stripPrefix("[").stripSuffix("]").split(",").map {
      case "hintRed" => HintStone.HintRed
      case "hintWhite" => HintStone.HintWhite
    }.toVector
    case _ => Vector.empty

 */
