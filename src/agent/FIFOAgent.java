package agent;

import world.Cell;

import java.util.Set;

public class FIFOAgent extends AbstractAgent {

    public FIFOAgent(Cell currentPosition, Set<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        super(currentPosition, targetPositions, rechargePosition, maxCapacity, actFuelConsumption);
    }

    @Override
    public boolean reasoning() {
        if (needRecharge()) {
            isGoToRecharge = true;
            currentAct = getActMoveTo(rechargePosition);
            return true;
        }

        isGoToRecharge = false;
        if (currentTarget == null) {
            // if no goal to pursuit, return false
            if (targetPositions.isEmpty()) {
                return false;
            }
            // if no current goal, adopt one
            randomAdoptGoal();
        }
        // finally set current action
        currentAct = getActMoveTo(currentTarget);
        return true;
    }

    @Override
    public void updateGoal() {
        if (currentTarget == null) {
            return;
        }
        if (currentTarget.equals(currentPosition)) {
            // add to achieved and remove from targets
            achievedGoalCells.add(currentTarget);
            targetPositions.remove(currentTarget);
            currentTarget = null;
        }
    }

    /**
     * pick a target position as current goal
     */
    private void randomAdoptGoal() {
        for (Cell targetPosition : targetPositions) {
            currentTarget = targetPosition;
            return;
        }
    }

    /**
     * Estimate whether the agent need to do recharge operation
     */
    private boolean needRecharge() {
        return currentFuel <= estimateFuelConsumption(rechargePosition);
    }
}
