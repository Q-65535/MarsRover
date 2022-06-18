package world;

import agent.AbstractAgent;
import agent.MoveAction;

import java.util.ArrayList;
import java.util.List;

public class Environment {

    Cell[][] map;
    int mapSize;
    Cell rechargePosition;
    AbstractAgent agent;
    /**
     * All the goals the agent has to achieve
     */
    ArrayList<Cell> goals;
    /**
     * Record how many times this environment has run
     */
    int runningCount;
    int actualActFuelConsumption;
    /**
     * set the default value to be 1, that is, post all goals at the beginning
     */
    int timeGapForPostGoal = 0;

    public Environment(int mapSize, Cell rechargePosition, AbstractAgent agent, int actualActFuelConsumption) {
        this.mapSize = mapSize;
        map = new Cell[mapSize][mapSize];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = new Cell(i, j);
            }
        }
        this.rechargePosition = rechargePosition;
        this.agent = agent;
        this.actualActFuelConsumption = actualActFuelConsumption;
    }

    public Environment(int mapSize, Cell rechargePosition, int actualActFuelConsumption, List<Cell> goals) {
        this(mapSize, rechargePosition, null, actualActFuelConsumption);
        this.goals = new ArrayList<>(goals);
    }

    public Environment(int mapSize, Cell rechargePosition, AbstractAgent agent, int actualActFuelConsumption, List<Cell> goals, int timeGapForPostGoal) {
        this(mapSize, rechargePosition, agent, actualActFuelConsumption);
        this.goals = new ArrayList<>(goals);
        this.timeGapForPostGoal = timeGapForPostGoal;
    }

    public Environment(int mapSize, Cell rechargePosition, AbstractAgent agent, int actualActFuelConsumption, List<Cell> goals) {
        this(mapSize, rechargePosition, agent, actualActFuelConsumption);
        this.goals = new ArrayList<>(goals);
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

    public boolean run() {
        // special condition: if the gap is set to be 0, post all goals at once
        if (timeGapForPostGoal == 0 && goals != null && !goals.isEmpty()) {
            postGoals(goals.size());
        }
        // at the beginning of the running, post 2 goals
        else if (isBeginning() && goals != null && !goals.isEmpty()) {
            postGoals(2);
        } else if (timeGapForPostGoal != 0 && runningCount % timeGapForPostGoal == 0 && goals != null && !goals.isEmpty()) {
            postGoals(1);
        }

        boolean runnable = false;
        boolean executable = agent.reason();
        // each time after the agent reasons, increment the running count
        runningCount++;
        if (executable) {
            runnable = true;
            MoveAction act = agent.execute();
            if (act == null) {
                throw new RuntimeException("no action to execute after reasoning");
            }
            executeAct(act);
        } else if (goals != null && !goals.isEmpty()) {
            runnable = true;
        }
        return runnable;
    }

    /**
     * Evaluate if it is the beginning of the environment
     */
    private boolean isBeginning() {
        return runningCount == 0;
    }

    /**
     * Post specific number of goals to the agent
     *
     * @param num the number of goals
     */
    private void postGoals(int num) {
        if (num > goals.size()) {
            throw new RuntimeException("can't post" + num + "goals from" + goals.size() + "goals");
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
        agent.consumeFuel(actualActFuelConsumption);
        agent.updateRecharge();
        agent.updatePunish();
    }


}
