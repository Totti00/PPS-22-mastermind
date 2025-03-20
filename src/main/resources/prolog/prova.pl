% Ritorna una sequenza random partendo da una lista di colori
b([],[]).
b(C,[E|T2]) :- member(E,C), b(C,T2), length([E|T2], Len), length(C, Len), !.


% Altro soluzione ma con la sicurezza che si ferma perché gli do un count
b(_, 0, []).
b(Colors, N, [H|T]) :- member(H, Colors), N1 is N - 1, b(Colors, N1, T).
