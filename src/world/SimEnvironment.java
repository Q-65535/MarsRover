package world;

import agent.AbstractAgent;
import agent.MCTSAgent;
import agent.MoveAction;

public class SimEnvironment extends Environment implements Cloneable {

    public SimEnvironment(int mapSize, Cell rechargePosition, MCTSAgent agent, int actualActFuelConsumption) {
        super(mapSize, rechargePosition, agent, actualActFuelConsumption);
    }

    // TODO dangerous cast?
    @Override
    public MCTSAgent getAgent() {
        MCTSAgent mctsAgent = (MCTSAgent) agent;
        return mctsAgent;
    }

    @Override
    public void executeAct(MoveAction act) {
        Cell currentPosition = agent.getCurrentPosition();
        int curX = currentPosition.getX();
        int curY = currentPosition.getY();
        switch (act) {
            case UP -> agent.updatePosition(curX, curY + 1);
            case DOWN -> agent.updatePosition(curX, curY - 1);
            case LEFT -> agent.updatePosition(curX - 1, curY);
            case RIGHT -> agent.updatePosition(curX + 1, curY);
        }
        // update internal state based on this new position
        agent.updateGoal();
        agent.consumeFuel(actualActFuelConsumption);
        agent.updateRecharge();
    }

    public void executeJump(Cell target) {
        Cell currentPosition = agent.getCurrentPosition();
        int distance = Calculator.calculateDistance(currentPosition, target);

        agent.updatePosition(target);
        agent.updateGoal();
        agent.consumeFuel(actualActFuelConsumption * distance);
        agent.updateRecharge();
    }

    @Override
    public SimEnvironment clone() {
        // clone agent
        MCTSAgent mctsAgent = (MCTSAgent) agent;
        MCTSAgent cloneAgent = mctsAgent.clone();
        // clone the environment
        return new SimEnvironment(mapSize, rechargePosition, cloneAgent, actualActFuelConsumption);
    }
}
