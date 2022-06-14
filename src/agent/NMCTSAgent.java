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
import java.util.Set;

public class NMCTSAgent extends MCTSAgent {

    public NMCTSAgent(Cell currentPosition, Set<Cell> targetPositions, HashMap<Cell, Norm> norms) {
        super(currentPosition, targetPositions, def_recharge_position, infinite_capacity, def_act_fuel_consumption);
        penalty = 0;
        this.norms = norms;
    }

    @Override
    AbstractState constructState(SimEnvironment simEnv) {
        return new NMarsRoverState(simEnv);
    }
}
