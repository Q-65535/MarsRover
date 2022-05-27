package MCTSstate;

import agent.AbstractAgent;
import agent.MCTSAgent;
import agent.MoveAction;
import world.Cell;
import world.Environment;
import world.SimEnvironment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MarsRoverState extends AbstractState {
    private SimEnvironment simEnv;
    private MCTSAgent simAgent;
    private Cell rechargePosition;

    public MarsRoverState(SimEnvironment simEnv) {
        this.simEnv = simEnv.clone();
        if (!(simEnv.getAgent() instanceof MCTSAgent)) {
            throw new RuntimeException("only mcts agent can be added to state");
        }
        rechargePosition = simEnv.getRechargePosition();
        simAgent = this.simEnv.getAgent();
    }

    @Override
    public MarsRoverState clone() {
        return new MarsRoverState(this.simEnv);
    }

    @Override
    public void exeAct(MoveAction act) {
        simEnv.executeAct(act);
    }

    @Override
    public void exeJump(Cell target) {
        simEnv.executeJump(target);
    }

    @Override
    public void exeJump(int x, int y) {
        simEnv.executeJump(new Cell(x, y));
    }

    @Override
    public double evaluateState() {
        return 100000 / simAgent.getTotalFuelConsumption() + 1;
    }


    @Override
    public AbstractState randomSim() {
        MarsRoverState cloneState = this.clone();
        MCTSAgent cloneAgent = cloneState.simAgent;
        Set<Cell> targetPositions = cloneAgent.getTargetPositions();
        while (!targetPositions.isEmpty()) {
            // get the nearest target
            Cell nearestTarget = null;
            int minConsumption = Integer.MAX_VALUE;
            for (Cell targetPosition : targetPositions) {
                int consumption = cloneAgent.estimateFuelConsumption(targetPosition);
                if (consumption < minConsumption) {
                    nearestTarget = targetPosition;
                    minConsumption = consumption;
                }
            }
            // get a random target
            ArrayList<Cell> targetList = new ArrayList<>(targetPositions);
            Cell randomTarget = targetList.get(rm.nextInt(targetList.size()));

            // stochastically choose the target to jump
            Cell selectedTarget = rm.nextDouble() < 0.9 ? nearestTarget : randomTarget;
//            selectedTarget = randomTarget;

            // evaluate resource consumption
            int totalConsumption = cloneAgent.estimateFuelConsumption(selectedTarget) + cloneAgent.estimateFuelConsumption(selectedTarget, cloneState.rechargePosition);
            if (totalConsumption > cloneAgent.getCurrentFuel()) {
                cloneState.exeJump(cloneState.rechargePosition);
            }

            cloneState.exeJump(selectedTarget);
//            cloneState.exeAct(cloneAgent.getActMoveTo(selectedTarget));
        }

        return cloneState;
    }

    @Override
    public ArrayList<MoveAction> getPossibleNextCs() {
        if (simAgent.getCurrentFuel() <= 0) {
            return null;
        }
        if (simAgent.getTargetPositions().size() == 0) {
            return null;
        }

        if (simAgent.needRecharge()) {
            return simAgent.getAllActMoveTo(simEnv.getRechargePosition());
        }

        Set<MoveAction> moveActions = new HashSet<>();
        // add move to recharge position actions
        moveActions.addAll(simAgent.getAllActMoveTo(simEnv.getRechargePosition()));
        // add all possible choices
        for (Cell targetPosition : simAgent.getTargetPositions()) {
            moveActions.addAll(simAgent.getAllActMoveTo(targetPosition));
            // if all 4 directions are included, immediately return
            if (moveActions.size() == 4) {
                return new ArrayList<>(moveActions);
            }
        }

        return new ArrayList<>(moveActions);
    }
}
