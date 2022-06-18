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
        double consumptionScale = 100;
        double penaltyScale = 1;

        double goalEval = simAgent.getNumOfAchieved();
        double consumptionEval = 50.0 / (simAgent.getTotalFuelConsumption() + 100);
        double penaltyEval = 0.5 / (simAgent.getTotalPenalty() + 1);

        return goalEval + consumptionEval + penaltyEval;
    }

    @Override
    public MarsRoverState clone() {
        return new NMarsRoverState(this.simEnv);
    }
}
