package mcts;

import MCTSstate.AbstractState;
import agent.AbstractAgent;
import agent.MoveAction;
import running.Utils;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractMCTSNode {
    static final double epsilon = 1e-6;
    static Random rm = new Random(Utils.SEED);

    AbstractState rootState;
    /**
     * The choice that leads the parent node to this node
     */
    MoveAction act;

    Statistic statistic = new Statistic();

    public AbstractMCTSNode(MoveAction act, AbstractState rootState) {
        this.rootState = rootState;
        this.act = act;
    }


    public abstract boolean isLeaf();

    public boolean isNotLeaf() {
        return !isLeaf();
    }

    public boolean hasChildren() {
        return isNotLeaf();
    }

    public abstract void expand(AbstractState sState);

    public abstract AbstractMCTSNode select();

    /**
     * exploit select child node, no exploration considered
     */
    public abstract AbstractMCTSNode exploitSelect();

    public abstract double rollOut(AbstractState sState);

    protected abstract double calUCT();

    public abstract AbstractMCTSNode randomChild();

    public abstract ArrayList<? extends AbstractMCTSNode> getChildren();

    public abstract AbstractState getCurrentState();
}

