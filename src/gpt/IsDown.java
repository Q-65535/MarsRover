package gpt;

import world.*;

// Is position term1 under term2?
public class IsDown extends DirectionCompare {

    public IsDown (PositionTerm term1, PositionTerm term2) {
        super(term1, term2);
    }

    @Override
    public boolean eval(Environment model) {
        Position p1 = insTerm1(model);
        Position p2 = insTerm2(model);

        return p1.getY() < p2.getY();
    }
}
