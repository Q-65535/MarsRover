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


    public SPMCTSAgent(List<Cell> goals, int maxCapacity) {
        super(goals, maxCapacity);
    }

    public SPMCTSAgent(int maxCapacity) {
        super(maxCapacity);
    }

    @Override
    AbstractMCTSNode constructNode() {
        return new SPNode();
    }
}
