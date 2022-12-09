package gpt;

import world.*;

public class Cross implements Formula {
    PositionTerm from;
    PositionTerm to;

    public Cross(Position from, Position to) {
	this.from = from;
	this.to = to;
    }

    public Position insTerm1(MarsRoverModel model) {
	return from.ins(model);
    }

    public Position insTerm2(MarsRoverModel model) {
	return to.ins(model);
    }

    @Override
    public boolean eval(MarsRoverModel model) {
	Position fromP = from.ins(model);
	Position toP = to.ins(model);
	return model.getPreAgentPosition().equals(fromP) &&
	    model.getCurAgentPosition().equals(toP);
    }

    @Override
    public String toString() {
	return from + "->" + to;
    }
}
