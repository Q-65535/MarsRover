package gpt;

import world.*;

// Is position term1 above term2?
public class IsUp extends DirectionCompare {

    public IsUp (PositionTerm term1, PositionTerm term2) {
        super(term1, term2);
    }


    @Override
    public boolean eval(MarsRoverModel marsRoverModel) {
        Position p1 = insTerm1(marsRoverModel);
        Position p2 = insTerm2(marsRoverModel);

        return p1.getY() > p2.getY();
    }
}
