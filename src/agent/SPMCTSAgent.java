package agent;

import mcts.AbstractMCTSNode;
import mcts.SPNode;
import gpt.Position;

import java.util.List;

public class SPMCTSAgent extends MCTSAgent {


    public SPMCTSAgent(List<Position> goals, int maxCapacity) {
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
