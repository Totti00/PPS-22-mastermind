compareToEqual([], [], []).
compareToEqual([H|CT], [H|UT], [hintRed|HintRest]) :- compareToEqual(CT, UT, HintRest), !.
compareToEqual([_|CT], [_|UT], HintRest) :- compareToEqual(CT, UT, HintRest).


compareToPresent(Code, UserInput, HintStones) :-
    exclude_correct_matches(Code, UserInput, FilteredCode, FilteredUserInput),
    compare_elements(FilteredCode, FilteredUserInput, [], HintStones), !.

% Confronta gli elementi rimanenti in UserInput
compare_elements(_, [], Hints, Hints).
compare_elements(Code, [U | UT], Acc, HintStones) :-
    count_occurrences(U, Code, Occurrences),
    Occurrences > 0,
    add_hints(Occurrences, Acc, NewAcc),
    remove_elements(U, [U | UT], RestUserInput),
    compare_elements(Code, RestUserInput, NewAcc, HintStones).
compare_elements(Code, [_ | UT], Acc, HintStones) :- compare_elements(Code, UT, Acc, HintStones).

% Aggiungi tanti HintWhite quanti sono le occorrenze 
add_hints(0, Acc, Acc).
add_hints(N, Acc, NewAcc) :- N > 0, N1 is N - 1, add_hints(N1, [hintWhite | Acc], NewAcc).

% Esclude gli elementi che sono nella posizione corretta
exclude_correct_matches([], [], [], []).
exclude_correct_matches([X | CT], [X | UT], FC, FU) :- exclude_correct_matches(CT, UT, FC, FU).
exclude_correct_matches([C | CT], [U | UT], [C | FC], [U | FU]) :- exclude_correct_matches(CT, UT, FC, FU).

% Rimuove le occorrenze data una lista
remove_elements(_, [], []).
remove_elements(E, [E|T], Rest) :- remove_elements(E, T, Rest).
remove_elements(E, [H|T], [H|Rest]) :- E \= H, remove_elements(E, T, Rest).

% Conta quante volte un elemento appare nella lista
count_occurrences(_, [], 0).
count_occurrences(E, [E|T], N) :- count_occurrences(E, T, N1), N is N1 + 1.
count_occurrences(E, [H|T], N) :- E \= H, count_occurrences(E, T, N).
