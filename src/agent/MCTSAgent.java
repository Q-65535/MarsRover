package agent;

import MCTSstate.*;
import gpt.Position;
import mcts.*;
import running.Default;
import world.SimEnvironment;

import java.util.List;

public class MCTSAgent extends AbstractAgent {
    // Whether the agent uses proactive strategy (The default value is false.).
    public boolean isProactive;
    MCTSWorkSpace ws = new MCTSWorkSpace();

    public MCTSAgent(List<GoalNode> goals, int maxCapacity) {
        super(goals, maxCapacity);
    }

    public MCTSAgent(int maxCapacity) {
        super(maxCapacity);
    }

    public MCTSAgent(int maxCapacity) {
        super(maxCapacity);
    }

    public setProactive() {
	this.isProactive = true;
    }

    @Override
    public boolean reason() {
        SimEnvironment simEnv = constructSimEnvironment();
        AbstractState rootState = constructState(simEnv);
        AbstractMCTSNode rootNode = constructNode();
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

    AbstractMCTSNode constructNode() {
        return new NaiveNode();
    }


    /**
     * reactive evaluation
     */
//    @Override
//    public boolean needRecharge() {
//        return currentFuel <= estimateFuelConsumption(rechargePosition);
//    }



    @Override
    public void updateGoal() {
        if (goals.contains(currentPosition)) {
            goals.remove(currentPosition);
            achievedGoals.add(currentGoal);
            // A goal is achieved
            isAchieved = true;
        }
    }


    @Override
    public MCTSAgent clone() {
        // clone new goal set and achieved list
        List<Position> cloneGoals = Default.cloneCells(goals);
        List<Position> cloneAchieved = Default.cloneCells(achievedGoals);

        MCTSAgent cloneAgent = new MCTSAgent(cloneGoals, maxCapacity, isProactive);

        // important: clone all the records
        cloneAgent.currentPosition = this.currentPosition;
        cloneAgent.currentFuel = this.currentFuel;
        cloneAgent.totalFuelConsumption = this.totalFuelConsumption;
        cloneAgent.rechargeFuelConsumption = this.rechargeFuelConsumption;
        cloneAgent.achievedGoals = cloneAchieved;
        cloneAgent.isAchieved = this.isAchieved;
        // clone norm information
        cloneAgent.norms = this.norms;
        cloneAgent.penalty = this.penalty;

        return cloneAgent;
    }
}
