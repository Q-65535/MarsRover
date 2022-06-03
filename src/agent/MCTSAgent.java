package agent;

import MCTSstate.MarsRoverState;
import mcts.MCTSWorkSpace;
import mcts.NaiveNode;
import running.Default;
import world.Cell;
import world.SimEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MCTSAgent extends AbstractAgent {
    List<MoveAction> bestActs;

    public MCTSAgent(Cell currentPosition, Set<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        super(currentPosition, targetPositions, rechargePosition, maxCapacity, actFuelConsumption);
        bestActs = new ArrayList<>();
    }

    @Override
    public boolean reasoning() {
        SimEnvironment simEnv = new SimEnvironment(mapSize, rechargePosition, this, actionFuelConsumption);
        MarsRoverState rootState = new MarsRoverState(simEnv);
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

    private boolean simulation() {
        return true;
    }


    /**
     * reactive evaluation
     */
    @Override
    public boolean needRecharge() {
        return currentFuel <= estimateFuelConsumption(rechargePosition);
    }



    @Override
    public void updateGoal() {
        if (targetPositions.contains(currentPosition)) {
            targetPositions.remove(currentPosition);
            achievedGoalCells.add(currentTarget);
        }
    }

    @Override
    public MCTSAgent clone() {
        // clone new goal set and achieved list
        Set<Cell> cloneTargets = Default.cloneCellSet(targetPositions);
        List<Cell> cloneAchieved = Default.cloneCellList(achievedGoalCells);

        MCTSAgent cloneAgent = new MCTSAgent(currentPosition, cloneTargets, rechargePosition, maxCapacity, actionFuelConsumption);

        // important: clone all the records
        cloneAgent.currentFuel = this.currentFuel;
        cloneAgent.totalFuelConsumption = this.totalFuelConsumption;
        cloneAgent.rechargeFuelConsumption = this.rechargeFuelConsumption;
        cloneAgent.achievedGoalCells = cloneAchieved;

        return cloneAgent;
    }
}
