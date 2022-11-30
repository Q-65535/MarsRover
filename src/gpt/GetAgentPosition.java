package gpt;

import world.*;

public class GetAgentPosition extends PositionFunction {

    @Override
    public Position ins(MarsRoverModel marsRoverModel) {
	return marsRoverModel.getAgentPosition();
    }
}
