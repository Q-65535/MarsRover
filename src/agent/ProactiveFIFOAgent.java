package agent;

import gpt.Position;

import java.util.List;

public class ProactiveFIFOAgent extends FIFOAgent {


    public ProactiveFIFOAgent(List<Position> goals, int maxCapacity) {
        super(goals, maxCapacity);
    }

    public ProactiveFIFOAgent(int maxCapacity) {
        super(maxCapacity);
    }

    @Override
    boolean needGiveup() {
        // if current fuel is less than 1, the agent can't do anything, give up
        if (currentFuel <= 0) {
            return true;
        }
        // if full fuel can't support the agent go from recharge position to goal and
        // come back, give up
        if (maxCapacity < 2 * estimateFuelConsumption(rechargePosition, currentGoal)) {
            return true;
        }
        return false;
    }

    /**
     * proactive evaluation
     */
    @Override
    public boolean needRecharge() {

        int goToGoalFuelConsumption = estimateFuelConsumption(currentGoal);
        int goalToDepotFuelConsumption = estimateFuelConsumption(currentGoal, rechargePosition);
        // we use < instead of == because if we use ==, the agent is confident that it can successfully achieve current goal
        boolean isProactiveTrigger = currentFuel < goToGoalFuelConsumption + goalToDepotFuelConsumption;
        return isProactiveTrigger;
    }
}
