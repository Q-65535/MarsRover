package agent;

import gpt.Position;

import java.util.List;

// Currently, greedy agent is not used.
public class GreedyAgent extends ProactiveFIFOAgent {

    public GreedyAgent(List<Position> goals, int maxCapacity) {
        super(goals, maxCapacity);
    }

    public GreedyAgent(int maxCapacity) {
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
            setNearestAsCurrentGoal();
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

        // finally set nearest and generate an action
        setNearestAsCurrentGoal();
        currentAct = getActMoveTo(currentGoal);
        return true;
    }
}
