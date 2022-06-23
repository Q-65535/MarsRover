package agent;

import world.Cell;
import world.Norm;

import java.util.*;

public class VBDIAgent extends NFIFOAgent {

    public VBDIAgent(List<Cell> goals, HashMap<Cell, Norm> norms) {
        super(goals, norms);
    }

    public VBDIAgent(HashMap<Cell, Norm> norms) {
        super(norms);
    }

    /**
     * get the movement action result in minimum norm penalty
     */
    @Override
    public MoveAction getActMoveTo(Cell goal) {
        HashMap<MoveAction, Double> actsPenalty = new HashMap<>();
        ArrayList<MoveAction> allActs = getAllActMoveTo(goal);
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
            // @remind in Mars rover scenario, the penalty is always a positive number
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
