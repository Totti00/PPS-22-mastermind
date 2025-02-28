---
title: Design di dettaglio
nav_order: 5
---

# Design di dettaglio

In questo capitolo verrà descritta nel dettaglio l'architettura del sistema, analizzandone i principali componenti e le 
rispettive caratteristiche.

## Component programming & cake Pattern
Come descritto nella sezione precedente, si è deciso di utilizzare il pattern architetturale MVC. Per agevolare l'implementazione di 
questa scelta si è deciso di utilizzare il Cake Pattern, questo permette di iniettare le dipendenza fra i vari componenti in modo 
semplice e dichiarativo utilizzando aspetti avanzati della programmazione funzionale tra cui: *self-type*, *min-in* e *type-members*.
Nello specifico ogni componente che si desidera implementare deve avere cinque aspetti principali:
1. Un trait che definisce l'interfaccia del componente;
2. Un trait `Provider` che definisce il riferimento al componente tramite una singleton-like val;
3. Un type-member `Requirements` che definisce, in modo dichiarativo, le dipendenze di altri componenti di cui ha bisogno per svolgere 
i propri compiti (queste verranno mixed-in dai provider degli altri componenti in modo automatico);
4. Un trait `Component` che definisce l'implementazione effettiva del componente;
5. Un trait `Interface` che si occupa di aggregare gli altri elementi del modulo per renderlo effettivamente utilizzabile.

Un esempio di modulo view implementato utilizzando questo pattern è il seguente:

```scala
object ViewModule:
  trait View:
    def show(stage: Stage): Unit
  
  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class ViewImpl extends View:
        private val GameView = new GameView(context)
        private var stage: Stage = _
        def show(primaryStage: Stage): Unit = {
          stage = primaryStage
          loadView("MenuPage")
        }
```

Questa strategia, sostanzialmente, prevede di implementare il *pattern MVC* come una composizione di tre elementi: Model (M), View (V) 
e Controller (C), i quali presentano le seguenti dipendenze: C -> V, V -> C e C -> M.
Più precisamente, possiamo realizzare questi tre elementi incapsulando già al loro interno la risoluzione delle dipendenze 
precedentemente citate e, infine, potremo istanziare un oggetto *MVC* che li detiene tutti e tre e che è in grado di accedere alle 
rispettive proprietà, senza doversi preoccupare del loro collegamento.

## Model

## View

## Controller

## Pattern utilizzati

## Organizzazione del codice
Il codice è stato struttura in package come descritto nel seguente diagramma