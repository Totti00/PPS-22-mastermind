---
title: Implementazione
nav_order: 6
---

# Implementazione
In questo capitolo è presente una discussione delle scelte implementative effettuate dai membri del team di sviluppo.

## Programmazione funzionale

Dato il contesto di sviluppo di questo progetto si è cercato di utilizzare il più possibile il paradigma funzionale. Di seguito 
sono elencati alcuni aspetti ritenuti rilevanti per comprendere come questo tipo di paradigma sia stato sfruttato.

### For comprehensions
Al fine di rendere il codice meno imperativo, si è fatto uso della *for-comprehension*: un costrutto funzionale per operare 
sulle collezioni e basato sulle *monadi*.

Oltre a rendere il codice più funzionale, la scelta dell'utilizzo della *for-comprehension* è supportata dall'incremento della 
leggibilità del codice. Il costrutto viene ad esempio utilizzato per iterare su un intervallo di valori in modo dichiarativo, 
raccogliendo gli elementi trasformati in una nuova collezione. Nel caso specifico, permette di ottenere, per ogni colonna, la pietra 
corrispondente al turno corrente, restituendo il risultato come un `Vector`.

```scala
val newCurrentTurnStones = (for (i <- 0 until cols) yield getStone(i, controller.turn)).toVector
updateGrid(attemptGrid, newCurrentTurnStones, identity)
```

### Pattern matching
Questo approccio consente di confrontare un valore con un determinato pattern, offrendo una sintassi altamente espressiva e 
idiomatica. Nel progetto, è stato impiegato prevalentemente nella sua forma più tradizionale, fungendo da versione avanzata e 
più potente dello *switch* di Java. Ad esempio:

```scala
path match
  case Game => loadFXML[GameView](path, Some(GameView(context.controller, stage.get)).get)
  case Rules => loadFXML[RulesView](path, RulesView(stage.get))
  case Menu  => loadFXML[MenuView](path, MenuView(context.controller, stage.get))
```

### Currying 
Confrontarsi con ramzi, perchè alla fine lo si utilizza solo in fromStringToVector ma ne parliamo già nel capitolo precedente 
nel pattern strategy perchè utilizza high order function  //TODOOOOOOOOOOOOOOOOOOOOOOOOOO

### High order function
L'utilizzo delle *higher-order functions* consente di definire funzioni che accettano come parametro un'altra funzione (o più di una) 
e/o restituiscono una funzione come risultato. Questo approccio facilita l'implementazione del *Strategy Pattern* e favorisce un 
maggiore riutilizzo del codice, migliorandone la modularità e la flessibilità.

```scala
def fromStringToVector[T <: Stone: ClassTag](list: String)(mapper: String => T): Vector[T] =
  if list == "[]" then Vector.empty
  else list.init.tail.split(",").map(mapper).toVector
```

### Type members
In Scala, la keyword `type` introduce il concetto di *type members* all'interno di una classe, affiancandoli ai tradizionali membri 
come *fields* e *methods*. Il suo utilizzo principale consiste nella definizione di alias per tipi complessi, consentendo al *type system* 
di sostituire automaticamente l'alias con il tipo effettivo durante il *type checking*.

Analogamente agli altri membri di una classe, i *type members* possono essere *abstract*, permettendo così di specificare il tipo 
concreto nell'implementazione.
Nel contesto del progetto, i *type members* sono stati impiegati per definire *PlayableStones* e *HintStones* come alias di 
`Vector[PlayerStone]` e `Vector[HintStone]`, semplificando l'uso e la comprensione delle collezioni all'interno della logica di gioco.

```scala
type PlayableStones = Vector[PlayerStone]
type HintStones = Vector[HintStone]
```

### Option
Nella programmazione funzionale, la classe `Option` viene utilizzata per rappresentare la possibile assenza di un valore, 
evitando l'uso di `null` e migliorando la sicurezza e la manutenibilità del codice.

Nel progetto, Option è stata impiegata per gestire variabili inizialmente dichiarate a livello globale, ma utilizzate solo in 
specifici punti del programma, garantendo così una gestione più sicura e controllata del loro stato. In particolare, all'interno 
della classe `ControllerModule`, Option è stata utilizzata nel metodo *updateView* per aggiornare dinamicamente la vista in base ai 
parametri forniti. Questo approccio consente di avere un'unica funzione per la gestione degli aggiornamenti, permettendo di passare 
il valore `vectorOfHintStones` solo quando effettivamente disponibile.

```scala
private def updateView(gameMode: GridUpdateType, vectorOfHintStones: Option[HintStones] = None): Unit =
  context.view.updateGameView(gameMode, vectorOfHintStones)
```

### Either
Questo meccanismo è utilizzato per gestire valori che potrebbero essere di due tipi diversi (disgiunti). Nel progetto è 
stato utilizzato in diversi contesti:

- **Model**: `Either` viene utilizzato per gestire un errore nel caso in cui l'utente scelga un livello di difficoltà non valido 
o fornisca una matrice con dimensioni errate. In tal caso, viene assegnata automaticamente una configurazione 
predefinita corrispondente alla difficoltà media. Nel codice sottostante è rappresentato uno dei due casi.
```scala
giveMeEither {
  require(rows >= 0 && cols >= 0, "Invalid matrix size")
  MatrixImpl(Vector.fill(rows, cols)(filling))
} match
  case Right(result) => result
  case Left(_)       => MatrixImpl(Vector.fill(MediumMode().boardSize._1, MediumMode().boardSize._2)(filling))
```

- **Controller**: qui viene impiegato per gestire in modo sicuro il recupero delle pedine di gioco, restituendo un errore in caso
  di richiesta non valida. Questo consente alla view di distinguere i casi di successo da quelli di errore e di gestire opportunamente
  la visualizzazione.
```scala
giveMeEither {
  typeStone.toLowerCase match
  case "playable" => context.model.getPlayableStone(row, col)
  case "hint"     => context.model.getHintStone(row, col)
}
```

- **Game**: il meccanismo viene sfruttato per garantire che la nuova configurazione della board sia coerente con quella attuale. 
Se i parametri non sono validi, il cambio di stato della board viene impedito.
```scala
giveMeEither {
  require(newBoard.rows == board.rows && newBoard.cols == board.cols, "Invalid arguments")
  this.board = newBoard
}
```

### Mixins
Il concetto di mixins consente di combinare più classi attraverso la composizione anziché ricorrere all'ereditarietà. Un 
esempio concreto di questa tecnica è implementato all'interno della classe ControllerModule:
```scala
trait Interface extends Provider with Component:
```

## Programmazione logica
Il team di sviluppo si è posto, come obiettivo per la realizzazione del progetto, l'utilizzo del paradigma logico. In fase 
di progettazione ci si è interrogati su come poter sfruttare la programmazione logica all'interno del progetto, scegliendo 
alcuni metodi che ben si adattavano alle caratteristiche di Prolog.

Per integrarlo in modo più efficiente e fluido nel progetto, è stato sviluppato un modulo che sfrutta un motore Prolog per
risolvere le query in ingresso. In questo modo, la soluzione fornita risulta sotto forma di oggetti SolveInfo. Per facilitare 
l’interazione con il motore, sono stati implementati metodi che eseguono il parsing dei risultati in formato stringa e li convertono 
in vettori di oggetti Stone.

Nello specifico, sono stati realizzati due file `.pl`: uno per la generazione del codice segreto e l'altro per la 
gestione della logica di confronto tra il codice e il tentativo.

### Utilizzo di Prolog per la generazione del codice segreto

Attraverso il *Prolog engine*, viene inizialmente creato un vettore contenente i colori che saranno utilizzati, seguito dalla 
generazione del codice segreto.
Utilizzando la regola *permutation*, è possibile ottenere tutte le possibili permutazioni della lista di colori, senza ripetizioni. 
Successivamente, una di queste permutazioni viene selezionata in modo casuale utilizzando Scala.
```prolog
member2([H|T], H, T).
member2([H|T], E, [H|O]) :- member2(T,E,O).

permutation(L, 0, []).
permutation(L, N, [E|T2]) :- N>0, member2(L, E, T), N1 is N-1, permutation(T, N1, T2).
```
Infine, viene impiegata la regola *codeGenerator* per generare tutte le possibili permutazioni di una lista di colori, inclusi i casi
con ripetizioni. Anche in questo caso, una delle possibilità viene selezionata casualmente utilizzando Scala.
```prolog
codeGenerator(_, 0, []).
codeGenerator(Colors, N, [H|T]) :- N > 0, member(H, Colors), N1 is N - 1, codeGenerator(Colors, N1, T).
```

### Utilizzo di Prolog per il confronto tra il codice segreto e il tentativo fornito dall'utente
Questo confronto è stato implementato tramite regole logiche in *Prolog* per generare gli indizi appropriati.
In particolare, la regola *compareToEqual* verifica la corrispondenza degli elementi nelle stesse posizioni, 
assegnando un `hintRed` per ognuna di esse. Gli elementi che non corrispondono vengono esclusi dal confronto successivo.

```prolog
compareToEqual([], [], []).
compareToEqual([C|CT], [U|UT], [hintRed|HintRest]) :- C == U, compareToEqual(CT, UT, HintRest), !.
compareToEqual([_|CT], [_|UT], HintRest) :- compareToEqual(CT, UT, HintRest).
```

La regola *compareToPresent*, invece, gestisce gli elementi che non sono nella posizione corretta. Dopo aver escluso quelli già 
corretti, vengono confrontati i rimanenti, calcolando le occorrenze di ciascun elemento nel codice segreto. Ogni corrispondenza 
parziale viene segnalata con un `hintWhite`.

```prolog
compareToPresent(Code, UserInput, HintStones) :-
    exclude_correct_matches(Code, UserInput, FilteredCode, FilteredUserInput),
    compare_elements(FilteredCode, FilteredUserInput, [], HintStones), !.
```

## Suddivisione del lavoro

Essendo il team di sviluppo composto da soli due membri, risulta complicato distinguere nettamente aree 
dell'applicazione attribuili a un solo membro piuttosto che all'altro: questo perché l'intero processo di sviluppo si è svolto in 
stretta collaborazione, perciò la quasi totalità degli elementi implementati è opera dell'intero team.

Durante lo svolgimento del progetto si è cercato di suddividere il carico di lavoro nella maniera più equa possibile, cercando di 
sfruttare le competenze di entrambi i membri del gruppo ed evitando che uno dei due svolgesse più lavoro dell'altro.

La mole di lavoro maggiore relativa alla parte di logica e view è stata svolta da Giacomo Totaro, mentre Ramzi Gallala ha 
contribuito in modo più significativo alla parte di model e al testing.

### Ramzi

### Totaro