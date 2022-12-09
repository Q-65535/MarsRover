package mcts;

import MCTSstate.AbstractState;
import agent.Choice;

import java.util.*;

public class NaiveNode extends AbstractMCTSNode {
    List<NaiveNode> children = new ArrayList<>();

    public NaiveNode() {
        super();
    }

    public NaiveNode(List<Choice> choices) {
        super(choices);
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public void expand(AbstractState sState) {
        List<List<Choice>> possibleNexts = sState.getPossibleNextCs();
        if (possibleNexts == null) {
            return;
        }
        // each possible choice corresponds to one MCTS node
        for (List<Choice> choices : possibleNexts) {
            NaiveNode child = new NaiveNode(choices);
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
    public List<? extends AbstractMCTSNode> getChildren() {
        return children;
    }
}
