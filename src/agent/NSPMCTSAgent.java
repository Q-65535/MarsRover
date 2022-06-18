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


    public NSPMCTSAgent(Cell initialPosition, List<Cell> targetPositions, HashMap<Cell, Norm> norms) {
        super(initialPosition, targetPositions, norms);
    }

    public NSPMCTSAgent(Cell initialPosition, HashMap<Cell, Norm> norms) {
        super(initialPosition, norms);
    }

    @Override
    AbstractMCTSNode constructNode(AbstractState rootState) {
        return new SPNode(rootState);
    }
}
