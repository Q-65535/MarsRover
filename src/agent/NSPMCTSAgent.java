package agent;

import MCTSstate.AbstractState;
import MCTSstate.MarsRoverState;
import MCTSstate.NMarsRoverState;
import mcts.AbstractMCTSNode;
import mcts.MCTSWorkSpace;
import mcts.SPNode;
import world.Cell;
import world.Norm;
import world.SimEnvironment;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class NSPMCTSAgent extends NMCTSAgent {


    public NSPMCTSAgent(List<Cell> goals, HashMap<Cell, Norm> norms) {
        super(goals, norms);
    }

    public NSPMCTSAgent(HashMap<Cell, Norm> norms) {
        super(norms);
    }

    @Override
    AbstractMCTSNode constructNode() {
        return new SPNode();
    }
}
