package world;

import gpt.*;

public class Norm {
    private final Position position;
    private final double penalty;

    public Norm(Position position, double penalty) {
        this.position = position;
        this.penalty = penalty;
    }

    // @Incomplete: waiting to be implemented.
    public boolean isViolation(ActionNode act) {
        return false;
    }

    public Position getGoalCell() {
        return position;
    }

    public double getPenalty() {
        return penalty;
    }
}
