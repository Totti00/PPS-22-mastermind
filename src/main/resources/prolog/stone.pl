% Altro soluzione ma con la sicurezza che si ferma perché gli do un count
codeGenerator(_, 0, []).
codeGenerator(Colors, N, [H|T]) :- N > 0, member(H, Colors), N1 is N - 1, codeGenerator(Colors, N1, T).

% trova tutte le permutazioni
member2([H|T], H, T).
member2([H|T], E, [H|O]) :- member2(T,E,O).

permutation(L, 0, []).
permutation(L, N, [E|T2]) :- N>0, member2(L, E, T), N1 is N-1, permutation(T, N1, T2).

