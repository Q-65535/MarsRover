package mcts;

import MCTSstate.AbstractState;
import agent.Choice;
import agent.MoveAction;

import java.util.ArrayList;
import java.util.List;

public class SPNode extends NaiveNode {
    static final double constC = 0.1;
    static final double constD = 32;


    public SPNode(List<Choice> choices) {
        super(choices);
    }

    public SPNode() {

    }

    // we override this method because we want the expanded nodes to be SPnodes instead of naive nodes.
    @Override
    public void expand(AbstractState sState) {
        List<List<Choice>> possibleNexts = sState.getPossibleNextCs();
        if (possibleNexts == null) {
            return;
        }
        // each possible choice corresponds to one MCTS node
        for (List<Choice> cs : possibleNexts) {
            SPNode child = new SPNode(cs);
            children.add(child);
        }
    }

    @Override
    protected NaiveNode select() {
        // initialisation
        NaiveNode selected = null;

        double bestUCT = Double.MIN_VALUE;
        // calculate the uct value for each of its selected nodes
        for (int i = 0; i < children.size(); i++) {

            // UCT calculation for single player MCTS
            double uctValue =
                    children.get(i).statistic.totValue / (children.get(i).statistic.nVisits + epsilon)
                            + constC * Math.sqrt(Math.log(statistic.nVisits + 1) / (children.get(i).statistic.nVisits + epsilon)) + rm.nextDouble() * epsilon
                            + Math.sqrt(
                            (children.get(i).statistic.totSquare - children.get(i).statistic.nVisits *
                                    (children.get(i).statistic.totValue / (children.get(i).statistic.nVisits + epsilon)) *
                                    (children.get(i).statistic.totValue / (children.get(i).statistic.nVisits + epsilon))
                                    + constD)
                                    / (children.get(i).statistic.nVisits + epsilon));
            // compare the uct value with the current maximum value
            if (uctValue > bestUCT) {
                selected = children.get(i);
                bestUCT = uctValue;
            }
        }
        // return the nodes with maximum UCT value, null if current node is a leaf node (contains no child nodes)
        return selected;
    }
}
