package agent;

import world.Cell;

import java.util.List;

public class FIFOAgent extends AbstractAgent {

    public FIFOAgent(List<Cell> goals, int maxCapacity) {
        super(goals, maxCapacity);
    }

    public FIFOAgent(int maxCapacity) {
        super(maxCapacity);
    }

    @Override
    public boolean reason() {
        if (currentGoal == null) {
            // if no goal to pursuit, return false
            if (goals.isEmpty()) {
                return false;
            }
            // if no current goal, adopt one
            setFirstAsCurrentGoal();
        }

        if (needGiveup()) {
            return false;
        }
	

        if (needRecharge()) {
            isGoToRecharge = true;
            currentAct = getActMoveTo(rechargePosition);
            return true;
        } else {
            isGoToRecharge = false;
        }

        // finally set current action
        currentAct = getActMoveTo(currentGoal);
        return true;
    }

    boolean needGiveup() {
        // if current fuel is less than 1, the agent can't do anything, give up
        if (currentFuel <= 0) {
            while (this != null) {
                System.out.println("This is crazy, no enough fuel, the agent just gave up!!!!");
            }
            return true;
        }
        return false;
    }

    @Override
    public void updateGoal() {
       if (currentGoal == null) {
            return;
        }
        // deal with current goal
        if (currentGoal.equals(currentPosition)) {
            // add to achieved and remove from goals
            achievedGoals.add(currentGoal);
            goals.remove(currentGoal);
            currentGoal = null;
        }
        // if the agent pass by a goal position, also achieve it
        if (goals.contains(currentPosition)) {
            achievedGoals.add(currentPosition);
            goals.remove(currentPosition);
        }
    }

    /**
     * set the first goal in the list of goals as current goal (FIFO)
     */
    void setFirstAsCurrentGoal() {
        // get the first element in the set
        currentGoal = goals.iterator().next();
    }

    /**
     * set the nearest goals in the list of goals as current goal (greedy)
     */
    void setNearestAsCurrentGoal() {
        currentGoal = getNearestGoal();
    }
}
