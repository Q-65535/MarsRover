package world;

import agent.AbstractAgent;
import agent.MoveAction;
import running.Utils;

public class Environment {

    private Cell[][] map;
    private Cell rechargePosition;
    private AbstractAgent agent;
    private int actualActFuelConsumption;

    public Environment(int mapSize, Cell rechargePosition, AbstractAgent agent, int actualActFuelConsumption) {
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

    private void executeAct(MoveAction act) {
        Cell currentPosition = agent.getCurrentPosition();
        int curX = currentPosition.getX();
        int curY = currentPosition.getY();
        switch (act) {
            case UP -> agent.updatePosition(curX, curY + 1);
            case DOWN -> agent.updatePosition(curX, curY - 1);
            case LEFT -> agent.updatePosition(curX - 1, curY);
            case RIGHT -> agent.updatePosition(curX + 1, curY);
        }
        agent.updateGoal();
        agent.consumeFuel(actualActFuelConsumption);
        agent.updateFuel();
    }
}
