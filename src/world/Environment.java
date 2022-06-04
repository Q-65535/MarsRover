package world;

import agent.AbstractAgent;
import agent.MoveAction;

public class Environment {

    Cell[][] map;
    int mapSize;
    Cell rechargePosition;
    AbstractAgent agent;
    int actualActFuelConsumption;

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

    public Cell get(int x, int y) {
        return map[x][y];
    }

    public Cell getRechargePosition() {
        return rechargePosition;
    }

    public AbstractAgent getAgent() {
        return agent;
    }

    public boolean run() {
        boolean runnable = false;
        boolean executable = agent.reasoning();
        if (executable) {
            runnable = true;
            MoveAction act = agent.execute();
            if (act == null) {
                throw new RuntimeException("no action to execute after reasoning");
            }
            executeAct(act);
        }
        return runnable;
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
