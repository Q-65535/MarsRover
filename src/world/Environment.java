package world;

import agent.AbstractAgent;
import agent.MoveAction;

import java.util.ArrayList;
import java.util.List;
import static running.Default.*;
public class Environment {

    Cell[][] map;
    final int mapSize = def_map_size;
    final Cell rechargePosition = def_recharge_position;
    AbstractAgent agent;
    /**
     * All the goals the agent has to achieve
     */
    ArrayList<Cell> goals;
    /**
     * Record how many times this environment has run
     */
    int runningCount = 0;
    int realActFuelConsumption;
    /**
     * set the default value to be 0, that is, post all goals at the beginning
     */
    int postInterval = 0;

    public Environment(AbstractAgent agent) {
        init();
        this.agent = agent;
    }

    public Environment(AbstractAgent agent, List<Cell> goals, int postInterval) {
        this(agent);
        this.goals = new ArrayList<>(goals);
        this.postInterval = postInterval;
    }

    public Environment(AbstractAgent agent, List<Cell> goals) {
        this(agent);
        this.goals = new ArrayList<>(goals);
    }

    public Environment(List<Cell> goals) {
        init();
        this.goals = new ArrayList<>(goals);
    }

    /**
     * Initialize the default settings
     */
    private void init() {
        map = new Cell[mapSize][mapSize];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = new Cell(i, j);
            }
        }
        this.realActFuelConsumption = def_act_consumption;
    }

    public Cell get(int x, int y) {
        return map[x][y];
    }

    public Cell getRechargePosition() {
        return rechargePosition;
    }

    public AbstractAgent getAgent() {
        return agent;
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


        // Here, we update the goal, considering the case when the poseted goal is just at agent's position!
        // @Smell: Maybe we can do this in a uniformed way.
        agent.updateGoal();
        boolean runnable = false;
        boolean executable = agent.reason();
        // each time after the agent reasons, increment the running count

       // long begin = System.nanoTime();               // ----------begin time record------------
        runningCount++;
        if (executable) {
            runnable = true;
            MoveAction act = agent.execute();
            if (act == null) {
                throw new RuntimeException("no action to execute after reasoning");
            }
            executeAct(act);
        } else if (haveGoal()) {
            runnable = true;
        }
       // System.out.println("env execute (include all but no reason) time cons: " + (System.nanoTime() - begin));    // ----------end time record------------
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

    void executeAct(MoveAction act) {
        Cell currentPosition = agent.getCurrentPosition();
        int curX = currentPosition.getX();
        int curY = currentPosition.getY();
        // first update new position
        switch (act) {
            case UP -> agent.updatePosition(curX, curY + 1);
            case DOWN -> agent.updatePosition(curX, curY - 1);
            case LEFT -> agent.updatePosition(curX - 1, curY);
            case RIGHT -> agent.updatePosition(curX + 1, curY);
        }
        // then, update internal state based on this new position
        agent.updateGoal();
        agent.consumeFuel(realActFuelConsumption);
        agent.updateRecharge();
        agent.updatePunish();
    }


}
