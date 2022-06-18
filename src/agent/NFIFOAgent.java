package agent;

import running.Default;
import world.Cell;
import world.Norm;

import java.util.HashMap;
import java.util.List;
import static running.Default.*;

public class NFIFOAgent extends FIFOAgent {

    public NFIFOAgent(Cell initPosition, List<Cell> targetPositions, HashMap<Cell, Norm> norms) {
        super(initPosition, targetPositions, Default.def_recharge_position, infinite_capacity, def_act_consumption);
        this.norms = norms;
    }

    public NFIFOAgent(Cell initialPosition, HashMap<Cell, Norm> norms) {
        this(initialPosition, null,  norms);
    }
}
