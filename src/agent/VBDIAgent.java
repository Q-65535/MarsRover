package agent;

import world.Cell;
import world.Norm;

import java.util.*;

public class VBDIAgent extends FIFOAgent {

    public VBDIAgent(int capacity) {
        super(capacity);
    }

    /**
     * get the movement action result in minimum norm penalty
     */
    @Override
    public MoveAction getRandomActMoveTo(Cell goal) {
        HashMap<MoveAction, Double> actsPenalty = new HashMap<>();
        ArrayList<MoveAction> allActs = getAllActMoveTo(goal);
        // record all norm information to actions
        for (MoveAction act : allActs) {
            // actsPenalty.put(act, normPenalty(act));
            actsPenalty.put(act, boundaryPenalty(act));
        }
        // get the act with the smallest penalty
        MoveAction smallestPenaltyAct = null;
        double smallestPenalty = Double.POSITIVE_INFINITY;
        for (Map.Entry<MoveAction, Double> actPenaltyPair : actsPenalty.entrySet()) {
            MoveAction act = actPenaltyPair.getKey();
            Double penalty = actPenaltyPair.getValue();
            // @Remind: in Mars rover scenario, the penalty is always a positive number.
            if (penalty < smallestPenalty) {
                smallestPenalty = penalty;
                smallestPenaltyAct = act;
            }
        }
        return smallestPenaltyAct;
    }


	private double boundaryPenalty(MoveAction act) {
        Cell nextPosition = getNextPosition(act);
		if (isViolateNorm(currentPosition, nextPosition)) {
			return crossSectorPenalty;
		} else {
			return 0;
		}
	}
}
