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
        double consumptionEval = 1.0 / (simAgent.getTotalFuelConsumption() + 1.0);
        return simAgent.getAchievedGoalCount() + consumptionEval;
    }

    @Override
    public AbstractState randomSim(List<MoveAction> actContainer) {
        MarsRoverState cloneState = this.clone();
        MCTSAgent cloneAgent = cloneState.simAgent;
        List<Cell> goals = cloneAgent.getGoals();
        while (!goals.isEmpty()) {
            // get the nearest goal
            Cell nearestGoal = cloneAgent.getNearestGoal();
            // get a random goal
            Cell randomGoal = goals.get(rm.nextInt(goals.size()));

            // stochastically choose a goal to jump
            Cell selectedGoal = rm.nextDouble() < 0.8 ? nearestGoal : randomGoal;

            /**
             * reactive simulation
             */
            // evaluate resource consumption
//            int goToRechargeConsumption = cloneAgent.estimateFuelConsumption(cloneAgent.getCurrentPosition(), cloneState.rechargePosition);
//
//            while (goToRechargeConsumption >= cloneAgent.getCurrentFuel()) {
//                // go to recharge action
//                MoveAction act = cloneAgent.getActMoveTo(cloneState.rechargePosition);
//                cloneState.exeAct(act);
//                actContainer.add(act);
//                // refresh the consumption amount
//                goToRechargeConsumption = cloneAgent.estimateFuelConsumption(cloneAgent.getCurrentPosition(), cloneState.rechargePosition);
//            }
//
//            while (!cloneAgent.getCurrentPosition().equals(selectedGoal)) {
//                goToRechargeConsumption = cloneAgent.estimateFuelConsumption(cloneAgent.getCurrentPosition(), cloneState.rechargePosition);
//                // If the fuel is not enough, first go back to recharge.
//                while (goToRechargeConsumption >= cloneAgent.getCurrentFuel()) {
//                    // go to recharge action
//                    MoveAction act = cloneAgent.getActMoveTo(cloneState.rechargePosition);
//                    cloneState.exeAct(act);
//                    actContainer.add(act);
//                    // refresh the consumption amount
//                    goToRechargeConsumption = cloneAgent.estimateFuelConsumption(cloneAgent.getCurrentPosition(), cloneState.rechargePosition);
//                }
//
//                MoveAction act = cloneAgent.getActMoveTo(selectedGoal);
//                cloneState.exeAct(act);
//                actContainer.add(act);
//            }

            /**
             * Proactive simulation
             */
            // evaluate resource consumption
           int totalConsumption = cloneAgent.estimateFuelConsumption(selectedGoal) + cloneAgent.estimateFuelConsumption(selectedGoal, cloneState.rechargePosition);

           while (totalConsumption > cloneAgent.getCurrentFuel()) {
               // go to recharge action
               MoveAction act = cloneAgent.getActMoveTo(cloneState.rechargePosition);
               cloneState.exeAct(act);
               actContainer.add(act);
               // refresh the consumption amount
               totalConsumption = cloneAgent.estimateFuelConsumption(selectedGoal) + cloneAgent.estimateFuelConsumption(selectedGoal, cloneState.rechargePosition);
           }

           while (!cloneAgent.getCurrentPosition().equals(selectedGoal)) {
               MoveAction act = cloneAgent.getActMoveTo(selectedGoal);
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
        for (Cell goal : simAgent.getGoals()) {
            moveActions.addAll(simAgent.getAllActMoveTo(goal));
            // if all 4 directions are included, immediately return
            if (moveActions.size() == 4) {
                return new ArrayList<>(moveActions);
            }
        }
        // Add the action to recharge
//       if (!simAgent.getCurrentPosition().equals(rechargePosition)) {
//           moveActions.add(simAgent.getActMoveTo(rechargePosition));
//       }

        return new ArrayList<>(moveActions);
    }
}
