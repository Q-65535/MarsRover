package gpt;

import world.*;

// Is position term1 at right of term2?
public class IsRight extends DirectionCompare {

    public IsRight (PositionTerm term1, PositionTerm term2) {
        super(term1, term2);
    }

    @Override
    public boolean eval(Environment model) {
        Position p1 = insTerm1(model);
        Position p2 = insTerm2(model);

        return p1.getX() > p2.getX();
    }
}
