package gpt;

import world.*;

public abstract class DirectionCompare implements Formula {
    private final PositionTerm term1;
    private final PositionTerm term2;



    // @Smell: Does it make sense to use an empty interface(PositionTerm should be an empty interface in this case.)?
    public DirectionCompare(PositionTerm term1, PositionTerm term2) {
        this.term1 = term1;
        this.term2 = term2;
    }

    // The instantiation doesn't affect this object because the terms are final.
    public Position insTerm1(MarsRoverModel marsRoverModel) {
        return term1.ins(marsRoverModel);
    }

    public Position insTerm2(MarsRoverModel marsRoverModel) {
        return term2.ins(marsRoverModel);
    }

    public abstract boolean eval(MarsRoverModel marsRoverModel);
}
