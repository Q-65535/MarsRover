package agent;

import gpt.Position;
import mcts.AbstractMCTSNode;
import mcts.SPNode;
import world.Norm;

import java.util.HashMap;
import java.util.List;

public class NSPMCTSAgent extends NMCTSAgent {


    public NSPMCTSAgent(List<Position> goals, HashMap<Position, Norm> norms) {
        super(goals, norms);
    }

    public NSPMCTSAgent(HashMap<Position, Norm> norms) {
        super(norms);
    }

    @Override
    AbstractMCTSNode constructNode() {
        return new SPNode();
    }
}
