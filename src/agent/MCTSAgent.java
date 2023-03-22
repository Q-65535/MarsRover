package agent;

import MCTSstate.AbstractState;
import MCTSstate.MarsRoverState;
import mcts.AbstractMCTSNode;
import mcts.MCTSWorkSpace;
import mcts.NaiveNode;
import running.Default;
import world.Cell;
import world.Norm;
import world.SimEnvironment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MCTSAgent extends AbstractAgent {
    // Whether the agent uses proactive strategy
    public boolean isProactive;
    MCTSWorkSpace ws = new MCTSWorkSpace();

    public MCTSAgent(List<Cell> goals, int maxCapacity, boolean isProactive) {
        super(goals, maxCapacity);
        this.isProactive = isProactive;
    }

    public MCTSAgent(int maxCapacity, boolean isProactive) {
        super(maxCapacity);
        this.isProactive = isProactive;
    }

    public MCTSAgent(int maxCapacity, HashMap<Cell, Norm> norms) {
        super(maxCapacity);
		this.norms = norms;
    }

    public MCTSAgent(int maxCapacity) {
        super(maxCapacity);
    }

	public void setIsProactive(boolean isProactive) {
		this.isProactive = isProactive;
	}

    @Override
    public boolean reason() {
//        long beginA = System.nanoTime();             //------------start time record-----------------
        SimEnvironment simEnv = constructSimEnvironment();
        AbstractState rootState = constructState(simEnv);
        AbstractMCTSNode rootNode = constructNode();
        ws.setRootState(rootState);
        ws.setRootMCTSNode(rootNode);
//        System.out.println("workspace construct time cons: " + (System.nanoTime() - beginA));       //-------------end time record------------------

        // run the MCTS process
//        long begin = System.nanoTime();             //------------begin time record-----------------
        ws.run(100, 10);
//        long timeCons = System.currentTimeMillis() - begin;
//        System.out.println("MCTS running time cons: " + (System.nanoTime() - begin));       //-------------end time record------------------

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
        List<Cell> cloneGoals = Default.cloneCells(goals);
        List<Cell> cloneAchieved = Default.cloneCells(achievedGoals);

        MCTSAgent cloneAgent = new MCTSAgent(cloneGoals, maxCapacity, isProactive);

        // @Important: clone all the records
        cloneAgent.currentPosition = this.currentPosition;
        cloneAgent.currentFuel = this.currentFuel;
        cloneAgent.totalFuelConsumption = this.totalFuelConsumption;
        cloneAgent.rechargeFuelConsumption = this.rechargeFuelConsumption;
        cloneAgent.achievedGoals = cloneAchieved;
        cloneAgent.isAchieved = this.isAchieved;
        // clone norm information
        cloneAgent.norms = this.norms;
		cloneAgent.boundaries = this.boundaries;
        cloneAgent.penalty = this.penalty;

        return cloneAgent;
    }
}
