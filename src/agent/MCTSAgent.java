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
    MCTSWorkSpace ws = new MCTSWorkSpace();

    public MCTSAgent(List<Cell> goals, int maxCapacity) {
        super(goals, maxCapacity);
    }

    public MCTSAgent(int maxCapacity) {
        super(maxCapacity);
    }

    @Override
    public boolean reason() {
        SimEnvironment simEnv = constructSimEnvironment();
        AbstractState rootState = constructState(simEnv);
        AbstractMCTSNode rootNode = constructNode(rootState);
        ws.setRootState(rootState);
        ws.setRootMCTSNode(rootNode);

        // run the MCTS process
        ws.run(100, 10);
        if (ws.hasNextBestAct()) {
            this.currentAct = ws.nextBestAct();
            return true;
        }
        return false;
    }

    SimEnvironment constructSimEnvironment() {
        return new SimEnvironment(this);
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
        List<Cell> cloneGoals = Default.cloneCells(goals);
        List<Cell> cloneAchieved = Default.cloneCells(achievedGoals);

        MCTSAgent cloneAgent = new MCTSAgent(cloneGoals, maxCapacity);

        // important: clone all the records
        cloneAgent.currentPosition = this.currentPosition;
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
