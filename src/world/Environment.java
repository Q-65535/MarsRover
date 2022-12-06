package world;

import agent.AbstractAgent;
import agent.MoveAction;
import gpt.*;

import java.util.ArrayList;
import java.util.List;

import static running.Default.*;

public class Environment {

    Position[][] map;
    final int mapSize = def_map_size;
    final Position rechargePosition = def_recharge_position;
    AbstractAgent agent;
    MarsRoverModel envMarsRoverModel;
    List<Norm> norms;
    /**
     * All the goals the agent has to achieve
     */
    ArrayList<AGoalNode> goals;
    /**
     * Record how many cycles this environment has run.
     */
    int runningCount;
    int realActFuelConsumption;
    /**
     * set the default value to be 0, that is, post all goals at the beginning
     */
    int postInterval = 0;

    public Environment(AbstractAgent agent) {
        init();
        this.agent = agent;
        // Sync the agent beliefbase.
        envMarsRoverModel.sync(agent.getBB());
    }

    public Environment(AbstractAgent agent, List<AGoalNode> goals, int postInterval) {
        this(agent);
        this.goals = new ArrayList<>(goals);
        this.postInterval = postInterval;
    }

    public Environment(AbstractAgent agent, List<AGoalNode> goals) {
        this(agent);
        this.goals = new ArrayList<>(goals);
    }

    public Environment(List<AGoalNode> goals) {
        init();
        this.goals = new ArrayList<>(goals);
    }

    /**
     * Initialize the default settings
     */
    private void init() {
        map = new Position[mapSize][mapSize];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = new Position(i, j);
            }
        }
        envMarsRoverModel = new MarsRoverModel();
        this.realActFuelConsumption = def_act_consumption;
    }

    public Position get(int x, int y) {
        return map[x][y];
    }

    public Position getRechargePosition() {
        return rechargePosition;
    }

    public AbstractAgent getAgent() {
        return agent;
    }

    public MarsRoverModel getModel() {
        return envMarsRoverModel;
    }

    public void setAgent(AbstractAgent agent) {
        this.agent = agent;
    }

    /**
     * Set the real fuel consumption in this environment
     */
    public void setRealFuelConsumption(int consumption) {
        this.realActFuelConsumption = consumption;
    }

    public boolean run() {
        // special condition: if the gap is set to be 0, post all goals at once
        if (isPostAllGoalsAtOnce() && haveGoal()) {
            postGoals(goals.size());
        }
        // at the beginning of the running, post 2 goals
        else if (isBeginning() && haveGoal()) {
            postGoals(2);
        } else if (!isPostAllGoalsAtOnce() && isTimeToPost() && haveGoal()) {
            postGoals(1);
        }


        boolean runnable = false;
        boolean executable = agent.reason();
        // each time after the agent reasons, increment the running count
        runningCount++;
        if (executable) {
            runnable = true;
            ActionNode act = agent.execute();
            if (act == null) {
                throw new RuntimeException("The agent says it is executable, but has no action to execute after reasoning");
            }
            boolean success = executeAct(act);
            if (success) {
                this.agent.exeSuccess();
            } else {
                this.agent.exeFail(envMarsRoverModel);
            }
        } else if (haveGoal()) {
            runnable = true;
        }
	// The agent senses the environment before next reasoning.
        agent.sense(this);
        return runnable;
    }

    /**
     * Evaluate whether there is goal available to post
     */
    private boolean haveGoal() {
        return (goals != null && !goals.isEmpty());
    }

    /**
     * Evaluate whether post all goals at once is set
     */
    private boolean isPostAllGoalsAtOnce() {
        // If the number of goals is 1, just post it at the beginning.
        return postInterval == 0 || goals.size() == 1;
    }

    /**
     * Evaluate if it is the beginning of the environment
     */
    private boolean isBeginning() {
        return runningCount == 0;
    }

    private boolean isTimeToPost() {
        return runningCount % postInterval == 0;
    }

    /**
     * Post specific number of goals to the agent
     *
     * @param num the number of goals
     */
    private void postGoals(int num) {
        if (num > goals.size()) {
            throw new RuntimeException("can't post " + num + " goals from " + goals.size() + " goals");
        }
        for (int i = 0; i < num; i++) {
            agent.adoptGoal(goals.remove(0));
        }
    }

    // This function applies the postcondition
    boolean executeAct(ActionNode act) {
        if (!envMarsRoverModel.eval(act.getPrec())) return false;
        // 1. update position and fuel level.
        envMarsRoverModel.apply(act.getPostc());
        // Then, lets check whether it is in recharge position.
        if (envMarsRoverModel.getAgentPosition().equals(rechargePosition)) {
            envMarsRoverModel.setAgentFuel(agent.getMaxCapacity());
        }

        //@Note: Seperate norm appling and postcondition appling.
//        applyNorm(act);
        return true;
    }

//    private void applyNorm(ActionNode act) {
//        for (Norm norm : norms) {
//            if (norm.isViolation(act)) {
//                // Environment doesn't tell which norm the agent violates; the agent just receive punishment.
//                agent.receivePunish(norm.getPenalty());
//            }
//        }
//    }
}
