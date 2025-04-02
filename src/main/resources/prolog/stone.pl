%nuovo modo per avere un codice

randNumFromList(List, Element) :- length(List, NumberOfElements), rand_int(NumberOfElements,N), Position is N+1, element(Position, List, Element) .

codeGenerator(_, 0, []).
codeGenerator(List, Length, [Element|Tail]) :- Length>0, randNumFromList(List, Element), NewLength is Length - 1, codeGenerator(List, NewLength, Tail).



%nuovo modo per avere una lista di colori

colors(List, 0, []).
colors(List, Length, [Element|Tail]) :- Length>0, randNumFromList(List, Element), delete(Element, List, ListDest), NewLength is Length-1, colors(ListDest, NewLength, Tail).

