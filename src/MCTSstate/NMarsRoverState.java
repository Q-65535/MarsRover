package MCTSstate;

import world.SimEnvironment;

public class NMarsRoverState extends MarsRoverState {

    public NMarsRoverState(SimEnvironment simEnv) {
        super(simEnv);
    }

    @Override
    public double evaluateState() {

//        double goalEval = simAgent.getAchievedGoalCount();
//        double consumptionEval = 50.0 / (simAgent.getTotalFuelConsumption() + 100);
//        double penaltyEval = 0.5 / (simAgent.getTotalPenalty() + 1);
//        return goalEval + consumptionEval + penaltyEval;

        double goalEval = simAgent.getAchievedGoalCount();
        double consumptionEval = simAgent.getTotalFuelConsumption();
        double penaltyEval = simAgent.getTotalPenalty();
        double val = simAgent.getAggregateVal();
//        double val = goalEval + 1.0 / (consumptionEval + penaltyEval + 1.0);
        return val;
    }

    @Override
    public MarsRoverState clone() {
        return new NMarsRoverState(this.simEnv);
    }
}
