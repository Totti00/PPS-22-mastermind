---
title: Sprint 1
parent: Backlog
nav_order: 1
---
# Sprint 1 [17/02/2025 - 23/02/2025]

| Backlog Item                    | Id  | Task                                                           | Volontario | Stima (h) | Effettivo (h) | D1 | D2 | D3 | D4 | D5 | D6 |
|:--------------------------------|:---:|:---------------------------------------------------------------|------------|:---------:|:-------------:|----|----|----|----|----|----|
| Setup repository                | 1.2 | Deploy su GitHub.io dell'applicazione                          | Team       |     1     |       1       | 1  | 0  | 0  | 0  | 0  | 0  |
| Setup repository                | 1.3 | Setup del code style del progetto (scalastyle)                 | Totaro     |     1     |       1       | 1  | 0  | 0  | 0  | 0  | 0  |
| Setup repository                | 1.4 | Setup scalatest & scoverage (deploy in ci)                     | Totaro     |     1     |       1       | 0  | 1  | 0  | 0  | 0  | 0  |
| Setup repository                | 1.5 | Setup Doc con deploy su GitHub.io                              | Totaro     |     1     |       1       | 0  | 1  | 0  | 0  | 0  | 0  |
| Setup repository                | 1.6 | Setup draw.io per diagrammi                                    | Ramzi      |     1     |       1       | 0  | 0  | 1  | 0  | 0  | 0  |
| Setup repository                | 1.7 | Setup scaladoc (deploy in ci)                                  | Ramzi      |     1     |       1       | 0  | 1  | 0  | 0  | 0  | 0  |
| Domain understanding & reporting | 2.1 | Definizione dominio in UML                                     | Team       |     4     |       4       | 0  | 0  | 4  | 0  | 0  | 0  |
| Domain understanding & reporting | 2.2 | Completare documentazione del dominio con diagrammi esportati  | Team       |     1     |       1       | 0  | 0  | 1  | 0  | 0  | 0  |
| Domain understanding & reporting | 2.3 | Scrivere requisiti nella documentazione                        | Team       |     2     |       2       | 0  | 0  | 0  | 2  | 0  | 0  |
| Domain understanding & reporting | 2.4 | Scrivere introduzione documentazione con spiegazione del gioco | Team       |     1     |       1       | 0  | 0  | 0  | 1  | 0  | 0  |
| Domain understanding & reporting | 2.5 | Scrivere processo adottato nella relazione                     | Team       |     2     |       2       | 0  | 0  | 0  | 0  | 0  | 0  |
| Architettura dell'applicativo   | 3.1 | Analisi dei pro e contro nei pattern architetturali            | Team       |     4     |       4       | 4  | 4  | 4  | 2  | 0  | 0  |
| Architettura dell'applicativo   | 3.2 | Implementazione architettura base                              | Team       |     2     |       2       | 2  | 2  | 2  | 2  | 2  | 0  |
|                                 |     | **TOT**                                                        |            |    30     |      30       | 23 | 21 | 20 | 16 | 4  | 0  |

## Sprint goal

Gli obiettivi di questo Sprint sono stati:

- Inizializziazione del progetto con relative configurazioni + C.I.
- Definizione di un primo dominio
- Definizione dell'architettura e prima implementazione

## Deadline

23/02/2025

## Sprint review

Lo Sprint goal è stato raggiunto secondo i tempi previsti.

## Sprint retrospective

Il primo sprint è stato incentrato sulla configurazione del progetto.

Inoltre anche la configurazione della C.I. ha richiesto parecchio tempo, questo a causa dei tempi elevati richiesti dal
deploy delle GitHub Actions, poichè si occupano di effettuare il deploy sia dell'applicazione, sia della documentazione
ma anche dello stile del codice, dei test e della coverage.