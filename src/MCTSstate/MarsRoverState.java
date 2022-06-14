package MCTSstate;

import agent.AbstractAgent;
import agent.MCTSAgent;
import agent.MoveAction;
import world.Cell;
import world.Environment;
import world.SimEnvironment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MarsRoverState extends AbstractState {
    SimEnvironment simEnv;
    MCTSAgent simAgent;
    Cell rechargePosition;

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
    public double evaluateState() {
        return 1000.0 / (simAgent.getTotalFuelConsumption() + 1);
    }


    @Override
    public AbstractState randomSim(List<MoveAction> actContainer) {
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
            Cell selectedTarget = rm.nextDouble() < 0.8 ? nearestTarget : randomTarget;
//            selectedTarget = randomTarget;

            // evaluate resource consumption
            int totalConsumption = cloneAgent.estimateFuelConsumption(selectedTarget) + cloneAgent.estimateFuelConsumption(selectedTarget, cloneState.rechargePosition);

            if (totalConsumption > cloneAgent.getCurrentFuel()) {
                while (!cloneAgent.getCurrentPosition().equals(cloneState.rechargePosition)) {
                    MoveAction act = cloneAgent.getActMoveTo(cloneState.rechargePosition);
                    cloneState.exeAct(act);
                    actContainer.add(act);
                }
            }

            while (!cloneAgent.getCurrentPosition().equals(selectedTarget)) {
                MoveAction act = cloneAgent.getActMoveTo(selectedTarget);
                cloneState.exeAct(act);
                actContainer.add(act);
            }
        }

        return cloneState;
    }

    @Override
    public ArrayList<MoveAction> getPossibleNextCs() {
        if (simAgent.getCurrentFuel() <= 0) {
            return null;
        }
        // no goals to pursuit
        if (simAgent.getTargetPositions().size() == 0) {
            return null;
        }
        // fuel is not enough
        if (simAgent.getCurrentFuel() < simAgent.estimateFuelConsumption(rechargePosition)) {
            return null;
        }

        if (simAgent.needRecharge()) {
            return simAgent.getAllActMoveTo(rechargePosition);
        }

        Set<MoveAction> moveActions = new HashSet<>();
        // add move to recharge position actions
        moveActions.addAll(simAgent.getAllActMoveTo(rechargePosition));
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
