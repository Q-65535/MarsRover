package mcts;

import MCTSstate.AbstractState;
import agent.MoveAction;
import running.Default;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractMCTSNode {
    static final double epsilon = 1e-6;
    Random rm = new Random(Default.SEED);
    /**
     * The choice that leads the parent node to this node
     */
    MoveAction act;

    Statistic statistic = new Statistic();

    public AbstractMCTSNode() {}

    public AbstractMCTSNode(MoveAction act) {
        this.act = act;
    }

    public abstract boolean isLeaf();

    public boolean isNotLeaf() {
        return !isLeaf();
    }

    public boolean hasChildren() {
        return isNotLeaf();
    }

    /** expand current node based on what choices the agent has in the given state.
     *  This method has side effects, it creates new node and set them as children of current node. */
    public abstract void expand(AbstractState sState);

    protected abstract AbstractMCTSNode select();

    public abstract AbstractMCTSNode randomChild();

    public abstract ArrayList<? extends AbstractMCTSNode> getChildren();

}

