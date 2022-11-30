package gpt;

import world.*;

// Is position term1 at right of term2?
public class IsRight extends DirectionCompare {

    public IsRight (PositionTerm term1, PositionTerm term2) {
        super(term1, term2);
    }

    @Override
    public boolean eval(MarsRoverModel marsRoverModel) {
        Position p1 = insTerm1(marsRoverModel);
        Position p2 = insTerm2(marsRoverModel);

        return p1.getX() > p2.getX();
    }
}
