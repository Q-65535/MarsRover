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

    public NMCTSAgent(Cell initialPosition, List<Cell> targetPositions, HashMap<Cell, Norm> norms) {
        super(initialPosition, targetPositions, def_recharge_position, infinite_capacity, def_act_consumption);
        this.norms = norms;
    }

    public NMCTSAgent(Cell initialPosition, HashMap<Cell, Norm> norms) {
        this(initialPosition, null,  norms);
    }

    @Override
    AbstractState constructState(SimEnvironment simEnv) {
        return new NMarsRoverState(simEnv);
    }
}
