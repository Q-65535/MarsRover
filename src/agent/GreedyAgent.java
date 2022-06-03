package agent;

import world.Cell;

import java.util.Set;

public class GreedyAgent extends ProactiveFIFOAgent {

    public GreedyAgent(Cell currentPosition, Set<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        super(currentPosition, targetPositions, rechargePosition, maxCapacity, actFuelConsumption);
    }

    @Override
    public boolean reasoning() {
        if (currentTarget == null) {
            // if no goal to pursuit, return false
            if (targetPositions.isEmpty()) {
                return false;
            }
            // if no current goal, adopt one
            adoptNearestGoal();
        }

        if (needGiveup()) {
            return false;
        }

        if (needRecharge()) {
            isGoToRecharge = true;
            currentAct = getActMoveTo(rechargePosition);
            return true;
        } else {
            isGoToRecharge = false;
        }

        // finally set current action
        currentAct = getActMoveTo(currentTarget);
        return true;
    }
}
