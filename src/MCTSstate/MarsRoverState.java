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
        // This evaluation considers norm and consumption.
		double val = simAgent.getAggregateVal();
		return val;

        // This evaluation only considers norm.
		// double normEval = 1.0 / (simAgent.getTotalPenalty() + 1.0);
		// return simAgent.getAchievedGoalCount() + normEval;

        // This evaluation only consider consumption.
      // double consumptionEval = 1.0 / (simAgent.getTotalFuelConsumption() + 1.0);
//       return simAgent.getAchievedGoalCount() + consumptionEval;
    }

    @Override
    public AbstractState randomSim(List<MoveAction> actContainer) {
//        long begin = System.nanoTime();
        MarsRoverState cloneState = this.clone();
        MCTSAgent cloneAgent = cloneState.simAgent;
        List<Cell> goals = cloneAgent.getGoals();
//        long timeCons = System.nanoTime() - begin;
//        System.out.println("clone time cons: " + timeCons);

//        begin = System.nanoTime();
        while (!goals.isEmpty()) {
            // get the nearest goal
            Cell nearestGoal = cloneAgent.getNearestGoal();
            // get a random goal
            Cell randomGoal = goals.get(rm.nextInt(goals.size()));

            // stochastically choose a goal to jump
            Cell selectedGoal = rm.nextDouble() < 0 ? nearestGoal : randomGoal;

            /**
             * reactive simulation
             */
            // evaluate resource consumption
            if (!cloneAgent.isProactive) {
                while (!cloneAgent.getCurrentPosition().equals(selectedGoal)) {
                    // If the fuel is not enough, first go back to recharge.
                    while (cloneAgent.needRecharge()) {
                        // go to recharge action
                        MoveAction act = cloneAgent.getRandomActMoveTo(cloneState.rechargePosition);
                        cloneState.exeAct(act);
                        actContainer.add(act);
                    }
                    MoveAction act = cloneAgent.getRandomActMoveTo(selectedGoal);
                    cloneState.exeAct(act);
                    actContainer.add(act);
                }
            }

            /**
             * Proactive simulation
             */
            else {
                // evaluate resource consumption
                int consumption = cloneAgent.estimateFuelConsumption(selectedGoal);
				// Proactive measure is taken if achieving current goal will violate mg.
                while (cloneAgent.getCurrentFuel() - consumption < 20) {
                    // go to recharge action
                    MoveAction act = cloneAgent.getRandomActMoveTo(cloneState.rechargePosition);
                    cloneState.exeAct(act);
                    actContainer.add(act);
                    // refresh the consumption amount
                    consumption = cloneAgent.estimateFuelConsumption(selectedGoal);
                }

                while (!cloneAgent.getCurrentPosition().equals(selectedGoal)) {
                    MoveAction act = cloneAgent.getRandomActMoveTo(selectedGoal);
                    cloneState.exeAct(act);
                    actContainer.add(act);
                }
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

        // Reactive recharge
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
        // Add the action to recharge at EVERY step
//       if (!simAgent.getCurrentPosition().equals(rechargePosition)) {
//           moveActions.add(simAgent.getActMoveTo(rechargePosition));
//       }

        // add goal in proactive manner
        if (!simAgent.getCurrentPosition().equals(rechargePosition) && simAgent.isAchieved && simAgent.isProactive) {
            moveActions.add(simAgent.getRandomActMoveTo(rechargePosition));
        }

        return new ArrayList<>(moveActions);
    }

    @Override
    public int getAchievedGoalCount() {
        return simAgent.getAchievedGoalCount();
    }
}
