package agent;

import MCTSstate.AbstractState;
import MCTSstate.MarsRoverState;
import MCTSstate.NMarsRoverState;
import mcts.MCTSWorkSpace;
import mcts.NaiveNode;
import world.Cell;
import world.Norm;
import world.SimEnvironment;
import static running.Default.*;

import java.util.HashMap;
import java.util.List;
import static running.Default.*;

public class NMCTSAgent extends MCTSAgent {

    public NMCTSAgent(List<Cell> goals, HashMap<Cell, Norm> norms) {
        super(goals, infinite_capacity, false);
        this.norms = norms;
    }

    public NMCTSAgent(HashMap<Cell, Norm> norms) {
        super(infinite_capacity);
        this.norms = norms;
    }

    @Override
    AbstractState constructState(SimEnvironment simEnv) {
        return new NMarsRoverState(simEnv);
    }
}
