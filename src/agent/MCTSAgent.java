package agent;

import MCTSstate.AbstractState;
import MCTSstate.MarsRoverState;
import mcts.AbstractMCTSNode;
import mcts.MCTSWorkSpace;
import mcts.NaiveNode;
import running.Default;
import world.Cell;
import world.SimEnvironment;

import java.util.List;

public class MCTSAgent extends AbstractAgent {
    MCTSWorkSpace ws;

    public MCTSAgent(Cell currentPosition, List<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        super(currentPosition, targetPositions, rechargePosition, maxCapacity, actFuelConsumption);
    }

    public MCTSAgent(Cell currentPosition, Cell rechargePosition, int maxCapacity, int actionFuelConsumption) {
        super(currentPosition, rechargePosition, maxCapacity, actionFuelConsumption);
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

//        MoveAction act = ws.bestActBasedOnChildren();
//        if (act == null) {
//            return false;
//        }
//        this.currentAct = act;
//        return true;

        if (ws.hasNextBestAct()) {
            this.currentAct = ws.nextBestAct();
            return true;
        }
        return false;
    }

    SimEnvironment constructSimEnvironment() {
        return new SimEnvironment(mapSize, rechargePosition, this, actFuelConsumption);
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
        if (goals.contains(currentPosition)) {
            goals.remove(currentPosition);
            achievedGoals.add(currentGoal);
        }
    }

    @Override
    public MCTSAgent clone() {
        // clone new goal set and achieved list
        List<Cell> cloneTargets = Default.cloneCells(goals);
        List<Cell> cloneAchieved = Default.cloneCells(achievedGoals);

        MCTSAgent cloneAgent = new MCTSAgent(currentPosition, cloneTargets, rechargePosition, maxCapacity, actFuelConsumption);

        // important: clone all the records
        cloneAgent.currentFuel = this.currentFuel;
        cloneAgent.totalFuelConsumption = this.totalFuelConsumption;
        cloneAgent.rechargeFuelConsumption = this.rechargeFuelConsumption;
        cloneAgent.achievedGoals = cloneAchieved;
        // clone norm information
        cloneAgent.norms = this.norms;
        cloneAgent.penalty = this.penalty;

        return cloneAgent;
    }
}
