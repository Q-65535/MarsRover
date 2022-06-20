package agent;

import running.Default;
import world.Cell;
import world.Norm;

import java.util.HashMap;
import java.util.List;
import static running.Default.*;

public class NFIFOAgent extends FIFOAgent {

    public NFIFOAgent(List<Cell> goals, HashMap<Cell, Norm> norms) {
        super(goals, infinite_capacity);
        this.norms = norms;
    }

    public NFIFOAgent(HashMap<Cell, Norm> norms) {
        super(infinite_capacity);
        this.norms = norms;
    }
}
