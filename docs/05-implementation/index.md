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
leggibilità del codice, come si può vedere nel seguente estratto di programma. Il costrutto viene ad esempio utilizzato per 
iterare su un intervallo di valori in modo dichiarativo, raccogliendo gli elementi trasformati in una nuova collezione. 
Nel caso specifico, permette di ottenere, per ogni colonna, la pietra corrispondente al turno corrente, restituendo il 
risultato come un `Vector`.

```scala
val newCurrentTurnStones = (for (i <- 0 until cols) yield getStone(i, controller.turn)).toVector
updateGrid(attemptGrid, newCurrentTurnStones, identity)
```
### pattern matching
Questo meccanismo permette di eseguire un match fra un valore e un dato pattern, ha una sintassi particolarmente idiomatica ed 
è stato usato principalmente nel modo classico, come fosse una sorta di switch di Java più potente. Ad esempio:

```scala
path match
  case Game => loadFXML[GameView](path, Some(GameView(context.controller, stage.get)).get)
  case Rules => loadFXML[RulesView](path, RulesView(stage.get))
  case Menu  => loadFXML[MenuView](path, MenuView(context.controller, stage.get))
```

### Currying 
Confrontarsi con ramzi, perchè alla fine lo si utilizza solo in fromStringToVector ma ne parliamo già nel capitolo precedente 
nel pattern strategy perchè utilizza high order function

### High order function
Confrontarsi con ramzi, perchè alla fine lo si utilizza solo in fromStringToVector ma ne parliamo già nel capitolo precedente
nel pattern strategy perchè utilizza high order function

### Type members
La keyword `type` in Scala introduce il concetto di *type members* all'interno di una classe, oltre ai *field* e *method* members che, 
solitamente, già troviamo. Viene impiegata principalmente per creare l'alias di un tipo più complicato: il *type system* sostituirà 
l'alias con l'*actual type* quando effettuerà il *type checking*.

I *type members*, analogamente agli altri membri delle classi, possono essere *abstract* ed è, dunque, possibile specificare il tipo 
concreto nell'implementazione.

In merito al progetto, i *type members* sono stati utilizzati per definire *PlayableStones* e *HintStones* come alias di 
`Vector[PlayerStone]` e `Vector[HintStone]`, semplificando l'uso e la comprensione delle collezioni delle pedine di gioco all'interno 
della logica del programma.

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
il valore `vectorOfHintStones` solo quando effettivamente disponibile, senza dover ricorrere a valori di default ambigui o a verifiche 
esplicite sulla presenza del dato.

```scala
private def updateView(gameMode: GridUpdateType, vectorOfHintStones: Option[HintStones] = None): Unit =
  context.view.updateGameView(gameMode, vectorOfHintStones)
```

### Either
Questo meccanismo è utilizzato per gestire valori che potrebbero essere di due tipi diversi (disgiunti). Nel progetto è 
stato utilizzato in diversi contesti:
- Controller: `Either` viene impiegato per gestire in modo sicuro il recupero delle pietre di gioco, restituendo un errore in caso 
di richiesta non valida. Questo consente alla view di distinguere i casi di successo da quelli di errore e di gestire opportunamente 
la visualizzazione.
```scala
giveMeEither {
  typeStone.toLowerCase match
  case "playable" => context.model.getPlayableStone(row, col)
  case "hint"     => context.model.getHintStone(row, col)
}
```
- Matrix: Verifica la validità delle dimensioni della matrice prima della sua creazione. Se i valori forniti non sono validi, 
viene assegnata una configurazione predefinita corrispondente alla difficoltà media.
```scala
giveMeEither {
  require(rows >= 0 && cols >= 0, "Invalid matrix size")
  MatrixImpl(Vector.fill(rows, cols)(filling))
} match
  case Right(result) => result
  case Left(_)       => MatrixImpl(Vector.fill(MediumMode().boardSize._1, MediumMode().boardSize._2)(filling))
```
- Game: Il meccanismo viene sfruttato per garantire che la nuova configurazione della board sia coerente con quella attuale. 
Se i parametri non sono validi, il cambio di stato della board viene impedito.
```scala
giveMeEither {
  require(newBoard.rows == board.rows && newBoard.cols == board.cols, "Invalid arguments")
  this.board = newBoard
}
```
- Model: `Either` viene utilizzato per determinare il livello di difficoltà selezionato dall'utente. Se il valore fornito non 
corrisponde a una difficoltà valida, il gioco assegna automaticamente la modalità media come fallback.
```scala
currentMode = giveMeEither {
  difficulty.toLowerCase match
  case "easy"    => EasyMode()
  case "medium"  => MediumMode()
  case "hard"    => HardMode()
  case "extreme" => ExtremeMode()
} match
  case Right(result) => result
  case Left(_)       => MediumMode()
```

### Mixins
Il meccanismo dei mixins permette di aggregare insieme più classi mediante composizione, invece che mediante ereditarietà.
Un esempio di utilizzo è presente nella classe `ControllerModule`:
```scala
trait Interface extends Provider with Component:
```

## Programmazione logica
Il team di sviluppo si è posto, come obiettivo per la realizzazione del progetto, l'utilizzo del paradigma logico. In fase 
di progettazione ci si è interrogati su come poter sfruttare la programmazione logica all'interno del progetto, giungendo alla 
conclusione di utilizzare Prolog come .......

Nello specifico, sono stati realizzati due file .pl`: 

### Utilizzo di Prolog per la generazione del codice segreto

### Utilizzo di Prolog per il check tra il codice segreto e il tentativo dell'utente

## Sezioni personali

### Ramzi

### Totaro



















Nel nostro progetto, il pattern Strategy è stato implementato utilizzando un'interfaccia (trait) GameMode e più classi concrete
(EasyMode, MediumMode, HardMode, ExtremeMode). Questo approccio consente di modellare le diverse modalità di gioco in modo
incapsulato e scalabile, mantenendo un'architettura chiara e facilmente estendibile.

Un esempio di utilizzo della strategia nel nostro codice è il seguente:

```scala
override def startNewGame(difficulty: String): Unit =
  currentMode = giveMeEither {
    difficulty.toLowerCase match
      case "easy"    => EasyMode()
      case "medium"  => MediumMode()
      case "hard"    => HardMode()
      case "extreme" => ExtremeMode()
  } match
    case Right(result) => result
    case Left(_)       => MediumMode()
```

In questo frammento, il modo di gioco viene selezionato dinamicamente in base alla difficoltà scelta dal giocatore, istanziando
l'oggetto corrispondente.
A differenza di un approccio basato su funzioni higher-order, in cui la strategia viene passata come parametro a una funzione,
il nostro approccio a classi consente di incapsulare lo stato e il comportamento di ogni modalità di gioco in modo autonomo,
rendendo più agevole la manutenzione e l'estensione del codice.

In contesti in cui le strategie sono puramente funzionali e prive di stato, l'uso di funzioni higher-order può risultare più conciso.
Tuttavia, nel nostro caso, il pattern Strategy basato su classi è risultato più adeguato, garantendo una maggiore chiarezza e
separazione delle responsabilità.