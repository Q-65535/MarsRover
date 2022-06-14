package MCTSstate;

import agent.MCTSAgent;
import agent.MoveAction;
import world.Cell;
import world.SimEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NMarsRoverState extends MarsRoverState {

    public NMarsRoverState(SimEnvironment simEnv) {
        super(simEnv);
    }

    @Override
    public double evaluateState() {
        double resourceValue = super.evaluateState();
        double penalty = simAgent.getTotalPenalty();

        return resourceValue + 0.1 * resourceValue * (1 / (penalty + 1));
    }

    @Override
    public MarsRoverState clone() {
        return new NMarsRoverState(this.simEnv);
    }
}
