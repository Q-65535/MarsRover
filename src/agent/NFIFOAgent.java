package agent;

import gpt.Position;
import world.Norm;

import java.util.HashMap;
import java.util.List;

public class NFIFOAgent extends FIFOAgent {

    public NFIFOAgent(List<Position> goals, HashMap<Position, Norm> norms) {
        super(goals, infinite_capacity);
        this.norms = norms;
    }

    public NFIFOAgent(HashMap<Position, Norm> norms) {
        super(infinite_capacity);
        this.norms = norms;
    }
}
