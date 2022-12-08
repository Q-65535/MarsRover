package gpt;

import world.*;

public class At implements AppliableFormula {
    private final PositionTerm term;

    public At(int x, int y) {
        this.term = new Position(x, y);
    }

    public At(PositionTerm p) {
        this.term = p;
    }

    @Override
    public boolean eval(MarsRoverModel marsRoverModel) {
        Position agentPosition = marsRoverModel.getCurAgentPosition();
        Position targetPosition = insTerm1(marsRoverModel);

        // @TODO: implement the equals function.
        return targetPosition.equals(agentPosition);
    }

    public Position insTerm1(MarsRoverModel marsRoverModel) {
        return term.ins(marsRoverModel);
    }

    @Override
    public void apply(MarsRoverModel marsRoverModel) {
	Position targetPosition = insTerm1(marsRoverModel);
	marsRoverModel.setCurAgentPosition(targetPosition);
    }

    public String toString() {
	if (term instanceof Position) {
	    Position p = (Position) term;
	    return "At" + p;
	} else {
	    return term.toString();
	}
    }
}
