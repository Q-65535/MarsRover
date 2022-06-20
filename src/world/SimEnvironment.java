package world;

import agent.AbstractAgent;
import agent.MCTSAgent;
import agent.MoveAction;

public class SimEnvironment extends Environment implements Cloneable {

    public SimEnvironment(MCTSAgent agent) {
        super(agent);
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
        agent.consumeFuel(realActFuelConsumption);
        agent.updateRecharge();
        agent.updatePunish();
    }

    /**
     * allow the agent to jump between loactions during the simulation for saving exection time.
     */
    public void executeJump(Cell goal) {
        Cell currentPosition = agent.getCurrentPosition();
        int distance = Calculator.calculateDistance(currentPosition, goal);

        agent.updatePosition(goal);
        agent.updateGoal();
        agent.consumeFuel(realActFuelConsumption * distance);
        agent.updateRecharge();
    }

    @Override
    public SimEnvironment clone() {
        // clone agent
        MCTSAgent mctsAgent = (MCTSAgent) agent;
        MCTSAgent cloneAgent = mctsAgent.clone();
        // clone the environment
        return new SimEnvironment(cloneAgent);
    }
}
