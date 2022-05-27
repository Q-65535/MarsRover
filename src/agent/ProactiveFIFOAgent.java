package agent;

import world.Cell;

import java.util.Set;

public class ProactiveFIFOAgent extends FIFOAgent {

    public ProactiveFIFOAgent(Cell currentPosition, Set<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        super(currentPosition, targetPositions, rechargePosition, maxCapacity, actFuelConsumption);
    }

    @Override
    boolean needGiveup() {
        // if current fuel is less than 1, the agent can't do anything, give up
        if (currentFuel <= 0) {
            return true;
        }
        // if full fuel can't support the agent go from recharge position to target and
        // come back, give up
        if (maxCapacity < 2 * estimateFuelConsumption(rechargePosition, currentTarget)) {
            return true;
        }
        return false;
    }

    /**
     * proactive evaluation
     */
    @Override
    boolean needRecharge() {
        boolean isReactiveTrigger = currentFuel <= estimateFuelConsumption(rechargePosition);

        int goToTargetFuelConsumption = estimateFuelConsumption(currentTarget);
        int targetToDepotFuelConsumption = estimateFuelConsumption(currentTarget, rechargePosition);
        // we use < because if ==, the agent is confident that it can successfully achieve current goal
        boolean isProactiveTrigger = currentFuel < goToTargetFuelConsumption + targetToDepotFuelConsumption;
        return isProactiveTrigger;
    }
}
