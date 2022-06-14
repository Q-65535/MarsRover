package agent;

import world.Cell;

import java.util.Set;

public class FIFOAgent extends AbstractAgent {

    public FIFOAgent(Cell currentPosition, Set<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        super(currentPosition, targetPositions, rechargePosition, maxCapacity, actFuelConsumption);
    }

    @Override
    public boolean reason() {
        if (currentTarget == null) {
            // if no goal to pursuit, return false
            if (targetPositions.isEmpty()) {
                return false;
            }
            // if no current goal, adopt one
            adoptRandomGoal();
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

    boolean needGiveup() {
        // if current fuel is less than 1, the agent can't do anything, give up
        if (currentFuel <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public void updateGoal() {
        if (currentTarget == null) {
            return;
        }
        // deal with current goal
        if (currentTarget.equals(currentPosition)) {
            // add to achieved and remove from targets
            achievedGoalCells.add(currentTarget);
            targetPositions.remove(currentTarget);
            currentTarget = null;
        }
        // if the agent pass by a goal position, also achieve it
        if (targetPositions.contains(currentPosition)) {
            achievedGoalCells.add(currentPosition);
            targetPositions.remove(currentPosition);
        }
    }

    /**
     * pick a target position as current goal
     */
    void adoptRandomGoal() {
        // get the first element in the set
        currentTarget = targetPositions.iterator().next();
    }

    void adoptNearestGoal() {
        Cell nearestTarget = null;
        int minConsumption = Integer.MAX_VALUE;
        for (Cell targetPosition : targetPositions) {
            int consumption = this.estimateFuelConsumption(targetPosition);
            if (consumption < minConsumption) {
                nearestTarget = targetPosition;
                minConsumption = consumption;
            }
        }

        currentTarget = nearestTarget;
    }


}
