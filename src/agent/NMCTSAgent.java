package agent;

import MCTSstate.AbstractState;
import MCTSstate.NMarsRoverState;
import gpt.Position;
import world.Norm;
import world.SimEnvironment;

import java.util.HashMap;
import java.util.List;

public class NMCTSAgent extends MCTSAgent {

    public NMCTSAgent(List<Position> goals, HashMap<Position, Norm> norms) {
        super(goals, infinite_capacity, false);
        this.norms = norms;
    }

    public NMCTSAgent(HashMap<Position, Norm> norms) {
        super(infinite_capacity);
        this.norms = norms;
    }

    @Override
    AbstractState constructState(SimEnvironment simEnv) {
        return new NMarsRoverState(simEnv);
    }
}
