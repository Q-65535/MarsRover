package mcts;

import MCTSstate.AbstractState;
import agent.MoveAction;

import java.util.ArrayList;

public class NaiveNode extends AbstractMCTSNode {
    private ArrayList<NaiveNode> children = new ArrayList<>();

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
    public NaiveNode select() {
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

    /**
     * Select the child node with maximum best value
     */
    @Override
    public AbstractMCTSNode exploitSelect() {
        NaiveNode selected = null;
        selected = children.get(rm.nextInt(children.size()));
        double bestValue = Double.NEGATIVE_INFINITY;
        for (NaiveNode child : children) {
            if (child.statistic.best > bestValue) {
                bestValue = child.statistic.best;
                selected = child;
            }
        }
        return selected;
    }

    @Override
    public double rollOut(AbstractState sState) {
        AbstractState endState = sState.randomSim();
        return endState.evaluateState();
    }

    @Override
    protected double calUCT() {
        double uctVal = statistic.totValue / (statistic.nVisits + epsilon) +
                Math.sqrt(Math.log(statistic.nVisits + 1) / (statistic.nVisits + epsilon)) + rm.nextDouble() * epsilon;
        return uctVal;
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

    @Override
    public AbstractState getCurrentState() {
        return null;
    }
}
