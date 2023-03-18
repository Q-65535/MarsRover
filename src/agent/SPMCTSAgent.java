package agent;

import mcts.AbstractMCTSNode;
import mcts.SPNode;
import world.Cell;

import java.util.List;

public class SPMCTSAgent extends MCTSAgent {


    public SPMCTSAgent(List<Cell> goals, int maxCapacity) {
        super(goals, maxCapacity, false);
    }

    public SPMCTSAgent(int maxCapacity) {
        super(maxCapacity);
    }

    @Override
    AbstractMCTSNode constructNode() {
        return new SPNode();
    }
}
