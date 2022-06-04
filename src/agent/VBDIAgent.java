package agent;

import world.Cell;
import world.Norm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VBDIAgent extends NFIFOAgent {

    public VBDIAgent(Cell initPosition, Set<Cell> targetPositions, HashMap<Cell, Norm> norms) {
        super(initPosition, targetPositions, norms);
    }

    /**
     * get the movement action result in minimum norm penalty
     */
    @Override
    public MoveAction getActMoveTo(Cell target) {
        HashMap<MoveAction, Double> actsPenalty = new HashMap<>();
        ArrayList<MoveAction> allActs = getAllActMoveTo(target);
        // record all norm information to actions
        for (MoveAction act : allActs) {
            actsPenalty.put(act, normPenalty(act));
        }
        // get the act with smallest penalty
        MoveAction smallestPenaltyAct = null;
        double smallestPenalty = Double.POSITIVE_INFINITY;
        for (Map.Entry<MoveAction, Double> actPenaltyPair : actsPenalty.entrySet()) {
            MoveAction act = actPenaltyPair.getKey();
            Double penalty = actPenaltyPair.getValue();
            if (penalty < smallestPenalty) {
                smallestPenalty = penalty;
                smallestPenaltyAct = act;
            }
        }
        return smallestPenaltyAct;
    }

    private double normPenalty(MoveAction act) {
        Cell nextPosition = getNextPosition(act);
        return normPenalty(nextPosition);
    }

    private double normPenalty(Cell position) {
        if (!norms.containsKey(position)) {
            return 0;
        }
        Norm relatedNorm = norms.get(position);
        return relatedNorm.getPenalty();
    }
}
