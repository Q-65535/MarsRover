package agent;

import MCTSstate.AbstractState;
import MCTSstate.MarsRoverState;
import MCTSstate.NMarsRoverState;
import mcts.AbstractMCTSNode;
import mcts.MCTSWorkSpace;
import mcts.NaiveNode;
import mcts.SPNode;
import world.Cell;
import world.SimEnvironment;

import java.util.List;
import java.util.Set;

public class SPMCTSAgent extends MCTSAgent {


    public SPMCTSAgent(Cell currentPosition, List<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        super(currentPosition, targetPositions, rechargePosition, maxCapacity, actFuelConsumption);
    }

    public SPMCTSAgent(Cell currentPosition, Cell rechargePosition, int maxCapacity, int actionFuelConsumption) {
        super(currentPosition, rechargePosition, maxCapacity, actionFuelConsumption);
    }

    @Override
    AbstractMCTSNode constructNode(AbstractState rootState) {
        return new SPNode(rootState);
    }
}
