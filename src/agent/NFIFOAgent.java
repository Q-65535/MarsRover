package agent;

import running.Default;
import world.Cell;
import world.Norm;

import java.util.HashMap;
import java.util.Set;

public class NFIFOAgent extends FIFOAgent {

    public NFIFOAgent(Cell initPosition, Set<Cell> targetPositions, HashMap<Cell, Norm> norms) {
        super(initPosition, targetPositions, Default.def_recharge_position, infinite_capacity, def_act_fuel_consumption);
        penalty = 0;
        this.norms = norms;
    }
}
