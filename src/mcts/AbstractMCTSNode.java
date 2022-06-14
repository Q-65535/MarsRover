package mcts;

import MCTSstate.AbstractState;
import agent.MoveAction;
import running.Default;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractMCTSNode {
    static final double epsilon = 1e-6;
    Random rm = new Random(Default.SEED);

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

    public AbstractMCTSNode(AbstractState rootState) {
        this.rootState = rootState;
    }

    public abstract boolean isLeaf();

    public boolean isNotLeaf() {
        return !isLeaf();
    }

    public boolean hasChildren() {
        return isNotLeaf();
    }

    public abstract void expand(AbstractState sState);

    protected abstract AbstractMCTSNode select();


    public abstract AbstractMCTSNode randomChild();

    public abstract ArrayList<? extends AbstractMCTSNode> getChildren();

}

