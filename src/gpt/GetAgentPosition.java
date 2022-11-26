package gpt;

import agent.*;
import world.*;

public class GetAgentPosition extends PositionFunction {

    @Override
    public Position ins(Environment model) {
        // @Smell: Why the f**k I'm using Cell and Position instead of just one class.
        AbstractAgent agent = model.getAgent();
        Cell c = agent.getCurrentPosition();
        return new Position(c.getX(), c.getY());

    }



}
