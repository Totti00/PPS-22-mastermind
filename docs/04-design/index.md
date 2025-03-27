---
title: Design di dettaglio
nav_order: 5
---

# Design di dettaglio

In questo capitolo verrà descritta nel dettaglio l'architettura del sistema, analizzandone i principali componenti e le 
rispettive caratteristiche.

## Component programming & cake Pattern
Come illustrato nella sezione precedente, si è scelto di adottare il pattern architetturale MVC. Per facilitare questa implementazione,
è stato impiegato il Cake Pattern, che consente di gestire le dipendenze tra i vari componenti in modo chiaro e strutturato, sfruttando
caratteristiche avanzate della programmazione funzionale come self-type, mix-in e type-members.
In particolare, ogni componente da implementare segue cinque principi fondamentali:
1. Un trait che definisce l'interfaccia del componente;
2. Un trait `Provider`, responsabile dell'esposizione del componente attraverso una variabile singleton-like;
3. Un type-member `Requirements`, che dichiara esplicitamente le dipendenze necessarie affinché il componente possa operare 
correttamente (queste verranno mixed-in dai provider degli altri componenti);
4. Un trait `Component`, che fornisce l'implementazione concreta del componente;
5. Un trait `Interface`, che combina e organizza gli elementi del modulo, rendendolo pronto per l'uso. 

Di seguito è riportato un esempio di modulo Controller realizzato secondo questo modello:
```scala
object ControllerModule:
  trait Controller:
    def goToPage(path: PagesEnum, mode: Option[String] = None): Unit
    def startGame(difficulty: String): Unit
  
  trait Provider:
    val controller: Controller

  type Requirements = ViewModule.Provider with ModelModule.Provider

  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      override def goToPage(path: PagesEnum, mode: Option[String] = None): Unit = context.view.loadView(path, mode)
      override def startGame(difficulty: String): Unit = context.model.startNewGame(difficulty)

  trait Interface extends Provider with Component:
    self: Requirements =>
```

## Model

![Model](../img/04-design/cake-model.jpg)
![Model_dettaglio](../img/04-design/model.jpg)


1 startNewGame
2 reset
3 Prendere le stone: sia hint che playable
4 sottomettere userInput
5 settere le stone per il nuovo turno
6 turni rimanenti
7 colori utilizzati

### Game

### Code

### Board

### Matrix

### GameState

### GameMode


## View
Immagine della struttura cake view
Parlare di come abbiamo fatto le view

## Controller
Immagine della struttura cake controller e basta

## Pattern utilizzati

### Strategy
Nel nostro progetto, il pattern Strategy è stato implementato utilizzando un'interfaccia (trait) GameMode e più classi concrete (EasyMode, MediumMode, HardMode, ExtremeMode). Questo approccio consente di modellare le diverse modalità di gioco in modo incapsulato e scalabile, mantenendo un'architettura chiara e facilmente estendibile.

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

In questo frammento, il modo di gioco viene selezionato dinamicamente in base alla difficoltà scelta dal giocatore, istanziando l'oggetto corrispondente.
A differenza di un approccio basato su funzioni higher-order, in cui la strategia viene passata come parametro a una funzione, il nostro approccio a classi consente di incapsulare lo stato e il comportamento di ogni modalità di gioco in modo autonomo, rendendo più agevole la manutenzione e l'estensione del codice.

In contesti in cui le strategie sono puramente funzionali e prive di stato, l'uso di funzioni higher-order può risultare più conciso. Tuttavia, nel nostro caso, il pattern Strategy basato su classi è risultato più adeguato, garantendo una maggiore chiarezza e separazione delle responsabilità.

## Organizzazione del codice
Il codice è stato struttura in package come descritto nel seguente diagramma