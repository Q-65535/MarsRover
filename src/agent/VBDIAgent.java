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
            actsPenalty.put(act, sectorPenalty(act));
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

    private double normPenalty(MoveAction act) {
        Cell nextPosition = getNextPosition(act);
        // If the agent is in norm position, and next position is also a norm position,
        // no penalty imposed (We simulate the slope scenario).
        if (norms.containsKey(currentPosition) && norms.containsKey(nextPosition)) {
            return 0;
        }
        return normPenalty(nextPosition);
    }

    private double normPenalty(Cell position) {
        if (!norms.containsKey(position)) {
            return 0;
        }
        Norm relatedNorm = norms.get(position);
        return relatedNorm.getPenalty();
    }

	private double sectorPenalty(MoveAction act) {
        Cell nextPosition = getNextPosition(act);
		Sector nextSector = getSector(nextPosition);
		if (isViolateNorm()) {
			return crossSectorPenalty;
		} else {
			return 0;
		}
	}
}
