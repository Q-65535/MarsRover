package agent;

import world.Cell;

import java.util.List;
import java.util.Set;

public class ProactiveFIFOAgent extends FIFOAgent {


    public ProactiveFIFOAgent(Cell currentPosition, List<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        super(currentPosition, targetPositions, rechargePosition, maxCapacity, actFuelConsumption);
    }

    public ProactiveFIFOAgent(Cell currentPosition, Cell rechargePosition, int maxCapacity, int actionFuelConsumption) {
        super(currentPosition, rechargePosition, maxCapacity, actionFuelConsumption);
    }

    @Override
    boolean needGiveup() {
        // if current fuel is less than 1, the agent can't do anything, give up
        if (currentFuel <= 0) {
            return true;
        }
        // if full fuel can't support the agent go from recharge position to target and
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
    boolean needRecharge() {

        int goToTargetFuelConsumption = estimateFuelConsumption(currentGoal);
        int targetToDepotFuelConsumption = estimateFuelConsumption(currentGoal, rechargePosition);
        // we use < instead of == because if we use ==, the agent is confident that it can successfully achieve current goal
        boolean isProactiveTrigger = currentFuel < goToTargetFuelConsumption + targetToDepotFuelConsumption;
        return isProactiveTrigger;
    }
}
