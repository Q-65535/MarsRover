package agent;

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
    public boolean reasoning() {
        SimEnvironment simEnv = new SimEnvironment(mapSize, rechargePosition, this, actionFuelConsumption);
        NMarsRoverState rootState = new NMarsRoverState(simEnv);
        //TODO is there a problem to pass null?
        NaiveNode rootNaiveNode = new NaiveNode(null, rootState);
        MCTSWorkSpace mctsWorkSpace = new MCTSWorkSpace(rootState, rootNaiveNode);
        mctsWorkSpace.run(100, 10);
        MoveAction act = mctsWorkSpace.bestChoice();

        if (act != null) {
            this.currentAct = act;
            return true;
        }
        return false;
    }
}
