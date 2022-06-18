package agent;

import world.Cell;

import java.util.List;
import java.util.Set;

public class GreedyAgent extends ProactiveFIFOAgent {

    public GreedyAgent(Cell currentPosition, List<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        super(currentPosition, targetPositions, rechargePosition, maxCapacity, actFuelConsumption);
    }

    public GreedyAgent(Cell currentPosition, Cell rechargePosition, int maxCapacity, int actionFuelConsumption) {
        super(currentPosition, rechargePosition, maxCapacity, actionFuelConsumption);
    }

    @Override
    public boolean reason() {
        if (currentGoal == null) {
            // if no goal to pursuit, return false
            if (goals.isEmpty()) {
                return false;
            }
            // if no current goal, adopt one
            setNearestAsCurrentGoal();
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

        // finally set nearest and generate an action
        setNearestAsCurrentGoal();
        currentAct = getActMoveTo(currentGoal);
        return true;
    }
}
