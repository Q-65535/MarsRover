package mcts;

import MCTSstate.AbstractState;
import agent.*;
import running.Default;
import gpt.*;

import java.util.*;

public abstract class AbstractMCTSNode {
    static final double epsilon = 1e-6;
    Random rm = new Random();
    /**
     * The choice that leads the parent node to this node
     */
    final List<Choice> choices;

    Statistic statistic = new Statistic();

    public AbstractMCTSNode() {
	this.choices = null;
    }

    public AbstractMCTSNode(List<Choice> choices) {
	this.choices = choices;
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

    public abstract List<? extends AbstractMCTSNode> getChildren();

}

