package gpt;

import world.*;

public class GetDiffPosition extends PositionFunction {
    PositionTerm basePosition;
    int xdiff;
    int ydiff;

    public GetDiffPosition (PositionTerm basePosition, int xdiff, int ydiff) {
        this.basePosition = basePosition;
        this.xdiff = xdiff;
        this.ydiff = ydiff;
    }

    @Override
    public Position ins(MarsRoverModel marsRoverModel) {
        Position c = marsRoverModel.getAgentPosition();
        int baseX = c.getX();
        int baseY = c.getY();

        int targetX = baseX + xdiff;
        int targetY = baseY + ydiff;
        return new Position(targetX, targetY);
    }
}
