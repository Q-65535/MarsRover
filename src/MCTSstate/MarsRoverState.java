package MCTSstate;

import agent.MCTSAgent;
import agent.MoveAction;
import world.Cell;
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

        double consumptionEval = 100.0 / (simAgent.getTotalFuelConsumption() + 100);
        return simAgent.getNumOfAchieved() + consumptionEval;
    }


    @Override
    public AbstractState randomSim(List<MoveAction> actContainer) {
        MarsRoverState cloneState = this.clone();
        MCTSAgent cloneAgent = cloneState.simAgent;
        List<Cell> targetPositions = cloneAgent.getGoals();
        while (!targetPositions.isEmpty()) {
            // get the nearest target
            Cell nearestTarget = cloneAgent.getNearestGoal();
            // get a random target
            ArrayList<Cell> targetList = new ArrayList<>(targetPositions);
            Cell randomTarget = targetList.get(rm.nextInt(targetList.size()));

            // stochastically choose a goal to jump
            Cell selectedTarget = rm.nextDouble() < 0.8 ? nearestTarget : randomTarget;

            // evaluate resource consumption
            int totalConsumption = cloneAgent.estimateFuelConsumption(selectedTarget) + cloneAgent.estimateFuelConsumption(selectedTarget, cloneState.rechargePosition);

            while (totalConsumption > cloneAgent.getCurrentFuel()) {
                // go to recharge action
                MoveAction act = cloneAgent.getActMoveTo(cloneState.rechargePosition);
                cloneState.exeAct(act);
                actContainer.add(act);
                // refresh the consumption amount
                totalConsumption = cloneAgent.estimateFuelConsumption(selectedTarget) + cloneAgent.estimateFuelConsumption(selectedTarget, cloneState.rechargePosition);
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
        if (simAgent.getGoals().size() == 0) {
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
        // add all possible choices
        for (Cell targetPosition : simAgent.getGoals()) {
            moveActions.addAll(simAgent.getAllActMoveTo(targetPosition));
            // if all 4 directions are included, immediately return
            if (moveActions.size() == 4) {
                return new ArrayList<>(moveActions);
            }
        }

        return new ArrayList<>(moveActions);
    }
}
