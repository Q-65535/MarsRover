package gpt;

import world.*;
import agent.*;

public class At implements Formula {
    private final PositionTerm term;

    public At(int x, int y) {
        this.term = new Position(x, y);
    }

    public At(PositionTerm p) {
        this.term = p;
    }

    public boolean eval(Environment model) {
        AbstractAgent agent = model.getAgent();
        Cell cell = agent.getCurrentPosition();
        Position agentPosition = new Position(cell.getX(), cell.getY());
        Position targetPosition = insTerm1(model);

        // @TODO: implement the equals function.
        return targetPosition.equals(agentPosition);
    }

    public Position insTerm1(Environment model) {
        return term.ins(model);
    }

}
