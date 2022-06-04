package MCTSstate;

import agent.MCTSAgent;
import world.Cell;
import world.SimEnvironment;

import java.util.ArrayList;
import java.util.Set;

public class NMarsRoverState extends MarsRoverState {

    public NMarsRoverState(SimEnvironment simEnv) {
        super(simEnv);
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

            // simulate all the movement actions
            while (!cloneAgent.getCurrentPosition().equals(selectedTarget)) {
                cloneState.exeAct(cloneAgent.getActMoveTo(selectedTarget));
            }
        }

        return cloneState;
    }

    @Override
    public double evaluateState() {
        return super.evaluateState() + (1 / (simAgent.getTotalPenalty() + 2));
    }

    @Override
    public MarsRoverState clone() {
        return new NMarsRoverState(this.simEnv);
    }
}
