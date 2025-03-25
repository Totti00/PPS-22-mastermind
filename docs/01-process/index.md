---
title: Processo di sviluppo
nav_order: 2
---
# Processo di sviluppo adottato
Il processo di sviluppo adottato si ispira alla metodologia SCRUM-inspired, scelta in conformità alle linee guida del corso per 
assicurare una gestione agile ed efficiente del progetto. Questo approccio prevede un'organizzazione iterativa e incrementale del 
lavoro, suddividendo lo sviluppo in sprint, durante i quali verranno progressivamente introdotte nuove funzionalità o perfezionate 
quelle già esistenti.

La qualità del progetto è garantita dall'esperienza del team di sviluppo, che segue le best practice di Project Management 
e Ingegneria del Software, avvalendosi sia di strumenti automatizzati che della collaborazione diretta tra i membri.

## Ruoli
Nella fase iniziale del processo sono stati definite le seguenti responsabilità:

- L'*esperto di dominio*: Giacomo Totaro, incaricato di garantire la qualità e l'usabilità del prodotto, oltre a contribuire allo 
sviluppo.
- Il *product owner*: Ramzi Gallala, incaricato di monitorare l'avanzamento del progetto, coordinare il team di sviluppo e 
contribuire allo sviluppo.

## Definition of Done
Una feature è ritenuta completa e idonea per l’integrazione nel branch main se soddisfa i seguenti requisiti:

- Tutti i test specifici della feature vengono superati con successo.
- L'intera suite di test del progetto esegue senza errori.
- La documentazione del codice è presente, se necessaria per agevolare la comprensione da parte dei revisori.

## Meeting
Le tipologie di meeting utilizzate sono le seguenti:

- **Initial planning**
  - **Descrizione**: questo incontro si tiene all'inizio del progetto e coinvolge tutti i membri del team.
  - **Obiettivo**: definire il *Product Backlog*, che raccoglie tutti i requisiti del progetto, e stabilire gli obiettivi per il 
  primo sprint.

- **Sprint planning**
  - **Descrizione**: riunione di pianificazione, della durata di circa un'ora, che si svolge all'inizio di ogni sprint e coinvolge 
  l'intero team.
  - **Obiettivo**: selezionare gli elementi del *Product Backlog* da sviluppare nello sprint e suddividerli in task, pianificando 
  le attività necessarie per raggiungere gli obiettivi dello sprint.

- **Daily Scrum**: 
  - **Descrizione**: breve incontro quotidiano (15-30 minuti), che si svolge dal martedì al venerdì e coinvolge tutti i membri 
  del team.
  - **Obiettivo**: aggiornare lo stato di avanzamento del progetto, condividere eventuali problemi riscontrati e favorire il 
  coordinamento tra i membri del team.

- **Sprint Review**:
  - **Descrizione**: riunioni di revisione della durata di circa un'ora, tenuta alla fine di ogni sprint e a cui partecipano 
  tutti i membri del team.
  - **Obiettivo**: analizzare il lavoro completato, raccogliere feedback e individuare possibili miglioramenti nel processo di 
  sviluppo.

## Trello
Per creare e gestire il Product Backlog, oltre che per organizzare le attività da svolgere, è stato scelto Trello, uno strumento 
basato sulla metodologia Kanban che facilita la gestione del progetto. Questo tool consente di assegnare i task agli sviluppatori 
e di suddividerli in liste, a seconda dello stato di avanzamento di ciascuna attività:
- **To Do**: contiene tutti i task da svolgere.
- **Doing**: contiene i task in corso di svolgimento.
- **Done**: contiene i task completati.

Grazie a questa struttura, ogni membro del team potrà avere una visione d'insieme sull'intero flusso di lavoro.

## DVCS
Per la gestione del codice sorgente su Git e GitHub, è stato scelto un modello che prevede l'utilizzo di due branch per 
organizzare lo sviluppo in modo strutturato:
- **main**: branch principale, utilizzato sia per lo sviluppo che per il rilascio di codice stabile.
- **develop**: branch di sviluppo, utilizzato per integrare le funzionalità sviluppate dai vari membri del team. 
- **doc**: branch dedicato alla documentazione, in cui vengono aggiornate le informazioni relative al progetto.

## Testing
Per garantire l'affidabilità delle funzionalità sviluppate, è stato adottato il paradigma del **Test Driven Development (TDD)**. 
Questo approccio consente di individuare e correggere tempestivamente eventuali errori nei singoli componenti del software 
durante lo sviluppo.

Il processo TDD si articola in tre fasi principali:
1. **Definizione del test**: si scrive un test che specifica il comportamento atteso del componente o delle funzionalità 
in sviluppo. Poiché l'implementazione non è ancora presente, deve inizialmente fallire.
2. **Implementazione**: si realizza il codice necessario affinché il test venga superato.
3. **Refactoring**: una volta che il test è stato superato, il codice viene ottimizzato e reso più leggibile, garantendo 
che i test continuino a essere validi anche dopo le modifiche.

## Building
Per la gestione del progetto, è stato scelto _Sbt_ come strumento di build, responsabile della gestione delle dipendenze 
necessarie.

Sbt semplifica anche l'esecuzione dei test, la generazione della documentazione, la compilazione del codice e la produzione di 
report relativi alla copertura dei test.

### CI/CD
Per la gestione della Continuous Integration e Continuous Deployment, è stato scelto di utilizzare GitHub Actions.

Sono stati configurati i seguenti workflow:
- **Test & CodeCheck**: esegue i test e verifica la formattazione del codice. Se tutti i test risultano superati, viene attivato 
il workflow Generate Scaladoc e Generate Coverage.
- **Generate Scaladoc**: si occupa di generare la scaladoc e di eseguire il push sul branch doc, per aggiornare la documentazione.
- **Generate Coverage**: si occupa di generare il report di coverage e di eseguire il push sul branch doc per aggiornare la 
documentazione.
- **Pages**: si occupa della generazione dell'artefatto web contenente la documentazione e del successivo caricamento su Github
Pages. Questo processo viene eseguito quando viene effettuato un push sul branch doc.

In caso di fallimento, lo strumento invia una notifica via email al membro del team che ha avviato il workflow, permettendo così 
di intervenire rapidamente.

### Code Quality
Per assicurare la qualità del codice, è stato scelto di utilizzare Scalafmt, uno strumento che formatta automaticamente il 
codice sorgente, migliorandone la leggibilità e la coerenza.

## Documentazione
La documentazione del progetto è stata redatta utilizzando il linguaggio Markdown, inizialmente scritta nell'apposito branch 
e successivamente caricata su GitHub Pages tramite il workflow definito in precedenza. La generazione del sito documentale
è stata realizzata con il tool [Jekyll](https://jekyllrb.com/), che consente di trasformare la documentazione in formato HTML.