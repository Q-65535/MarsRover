package mcts;

import MCTSstate.AbstractState;
import agent.MoveAction;

import java.util.ArrayList;

public class NaiveNode extends AbstractMCTSNode {
    ArrayList<NaiveNode> children = new ArrayList<>();

    public NaiveNode(AbstractState rootState) {
        super(rootState);
    }

    public NaiveNode(MoveAction act, AbstractState rootState) {
        super(act, rootState);
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public void expand(AbstractState sState) {
        ArrayList<MoveAction> possibleNexts = sState.getPossibleNextCs();
        if (possibleNexts == null) {
            return;
        }
        // each possible choice corresponds to one MCTS node
        for (MoveAction possibleNext : possibleNexts) {
            // TODO here, we always store the root state?
            NaiveNode child = new NaiveNode(possibleNext, rootState);
            children.add(child);
        }
    }

    /**
     * Select the child with the maximum UCT value
     */
    @Override
    protected NaiveNode select() {
        NaiveNode selected = null;
        // first random selection
        selected = children.get(rm.nextInt(children.size()));
        double bestUCT = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < children.size(); i++) {
            // UCT calculation
            double uctValue = children.get(i).statistic.totValue / (children.get(i).statistic.nVisits + epsilon) +
                    Math.sqrt(Math.log(statistic.nVisits + 1) / (children.get(i).statistic.nVisits + epsilon)) + rm.nextDouble() * epsilon;
            // compare the uct value with the current maximum value
            if (uctValue > bestUCT) {
                selected = children.get(i);
                bestUCT = uctValue;
            }
        }
        return selected;

    }

    @Override
    public AbstractMCTSNode randomChild() {
        if (!hasChildren()) {
            throw new RuntimeException("this node has no child!");
        }
        int index = rm.nextInt(children.size());
        return children.get(index);
    }

    @Override
    public ArrayList<NaiveNode> getChildren() {
        return children;
    }
}
