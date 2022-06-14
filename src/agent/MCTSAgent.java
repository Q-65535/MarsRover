package agent;

import MCTSstate.AbstractState;
import MCTSstate.MarsRoverState;
import mcts.AbstractMCTSNode;
import mcts.MCTSWorkSpace;
import mcts.NaiveNode;
import running.Default;
import world.Cell;
import world.SimEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MCTSAgent extends AbstractAgent {
    MCTSWorkSpace ws;

    public MCTSAgent(Cell currentPosition, Set<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        super(currentPosition, targetPositions, rechargePosition, maxCapacity, actFuelConsumption);
    }

    @Override
    public boolean reason() {
        SimEnvironment simEnv = constructSimEnvironment();
        AbstractState rootState = constructState(simEnv);
        AbstractMCTSNode rootNode = constructNode(rootState);
        // if it is the first time to run mcts
        if (ws == null) {
            ws = new MCTSWorkSpace(rootState, rootNode);
        } else {
            ws.setRootState(rootState);
            ws.setRootMCTSNode(rootNode);
        }
        ws.run(100, 10);

        if (ws.hasNextBestAct()) {
            this.currentAct = ws.nextBestAct();
            return true;
        }
        return false;
    }

    SimEnvironment constructSimEnvironment() {
        return new SimEnvironment(mapSize, rechargePosition, this, actionFuelConsumption);
    }

    AbstractState constructState(SimEnvironment simEnv) {
        return new MarsRoverState(simEnv);
    }

    AbstractMCTSNode constructNode(AbstractState rootState) {
        return new NaiveNode(rootState);
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
        // clone norm information
        cloneAgent.norms = this.norms;
        cloneAgent.penalty = this.penalty;

        return cloneAgent;
    }
}
