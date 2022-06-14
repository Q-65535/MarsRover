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

import java.util.Set;

public class SPMCTSAgent extends MCTSAgent {

    public SPMCTSAgent(Cell currentPosition, Set<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        super(currentPosition, targetPositions, rechargePosition, maxCapacity, actFuelConsumption);
    }

    @Override
    AbstractMCTSNode constructNode(AbstractState rootState) {
        return new SPNode(rootState);
    }
}
