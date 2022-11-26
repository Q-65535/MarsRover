package gpt;

import world.*;

// Is position term1 at left of term2?
public class IsLeft extends DirectionCompare {

    public IsLeft (PositionTerm term1, PositionTerm term2) {
        super(term1, term2);
    }

    @Override
    public boolean eval(Environment model) {
        Position p1 = insTerm1(model);
        Position p2 = insTerm2(model);

        return p1.getX() < p2.getX();
    }
}
