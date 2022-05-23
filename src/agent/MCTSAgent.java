package agent;

import world.Cell;

import java.util.Set;

public class MCTSAgent extends AbstractAgent{

    public MCTSAgent(Cell currentPosition, Set<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        super(currentPosition, targetPositions, rechargePosition, maxCapacity, actFuelConsumption);
    }

    @Override
    public boolean reasoning() {
        return false;
    }

    @Override
    public void updateGoal() {

    }
}
