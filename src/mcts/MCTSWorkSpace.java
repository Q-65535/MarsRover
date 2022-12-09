package mcts;

import MCTSstate.*;
import agent.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MCTSWorkSpace {
    /**
     * The initial state to run the MCTS search. Initial state corresponds to MCTS root node.
     */
     AbstractState rootState;

     AbstractMCTSNode rootMCTSNode;

    /**
     * action sequences lead to the best evaluation score
     */
    public List<Choice> bestChoices;

    int curAchievedGoalCount = 0;

    /**
     * Best simulation result record
     */
     double bestSimulationResult = Double.NEGATIVE_INFINITY;

    public MCTSWorkSpace() {
        bestChoices = new ArrayList<>();
    }

    public void setRootState(AbstractState rootState) {
        this.rootState = rootState;
    }

    public void setRootMCTSNode(AbstractMCTSNode rootMCTSNode) {
        this.rootMCTSNode = rootMCTSNode;
    }

    /**
     * Get the next few choices that will finally execute an action.
     */
    public List<Choice> popThisCycleChoices() {
	List<Choice> cs = new ArrayList<>();
	while(bestChoices.size() > 0) {
	    Choice c = bestChoices.remove(0);
	    cs.add(c);
	    // Hit an action execution choice. This means that choices for current cycle is constructed,
	    // just break.
	    if (c.isActionExecution()) {
		break;
	    }
	}
	return cs;
    }


    /**
     * The best child of root node based on the node's value. Notice that this evaluation is not based on UCT value, we
     * don't consider exploration at all.
     */
    public AbstractMCTSNode bestChild() {
        // if the root node cannot be expanded any further
        List<? extends AbstractMCTSNode> children = rootMCTSNode.getChildren();
        if (children.size() == 0) {
            return null;
        }
        // otherwise, find the child node that has been visited most
        else {
            int maxVisit = children.get(0).statistic.nVisits;
            double best = children.get(0).statistic.best;
            double total = children.get(0).statistic.totValue;
            double average = children.get(0).statistic.totValue / children.get(0).statistic.nVisits;
            AbstractMCTSNode bestChild = children.get(0);
            for (AbstractMCTSNode child : children) {
                if (child.statistic.totValue / child.statistic.nVisits > average) {
//                    if(child.statistic.totValue > total){
//                    if(child.statistic.nVisits > maxVisit){
//                    if(child.statistic.best > best){
                    maxVisit = child.statistic.nVisits;
                    best = child.statistic.best;
                    total = child.statistic.totValue;
                    average = child.statistic.totValue / child.statistic.nVisits;
                    bestChild = child;
                }
            }

            return bestChild;
        }
    }

    /**
     * Do the MCTS process, alpha times selection and beta times simulation
     */
    public void run(int alpha, int beta) {
        for (int i = 0; i < alpha; i++) {
            List<AbstractMCTSNode> visited = new LinkedList<>();
            // this copied state will be iteratively updated
            AbstractState sState = rootState.clone();
            // selection
            AbstractMCTSNode curNode = doSelectionPhase(sState, visited);
            // expansion
            curNode.expand(sState);
            // random selection and rollout
            if (curNode.hasChildren()) {
                AbstractMCTSNode rollOutNode = randomSelectChildAndUpdateState(curNode, sState);
                // record and update according to the randomly selected node
                visited.add(rollOutNode);

                // beta times simulation
                for (int j = 0; j < beta; j++) {
                    AbstractState endState = rollOut(sState);
                    int achievedGoalCount = endState.getAchievedGoalCount();
                    double simulationVal = endState.evaluateState();
                    // update the best simulation result and action list
                    // If the simulation value is greater than the record, and if the #goals is greater than the record, update
                    if (bestSimulationResult < simulationVal || curAchievedGoalCount < achievedGoalCount) {
                        curAchievedGoalCount = achievedGoalCount;
                        bestSimulationResult = simulationVal;
                        // Update the best choices list.
                        bestChoices = new ArrayList<>(endState.getChoicePath());
                    }
                    // back propagation
                    for (AbstractMCTSNode node : visited) {
                        node.statistic.addValue(simulationVal);
                    }
                }
                // if current node has no leaf node
            } else {
                double val = sState.evaluateState();
                for (AbstractMCTSNode node : visited) {
                    node.statistic.addValue(val);
                }
            }
        }
    }

    /**
     * simulate the progression from the given state to an end state and add all the actions with greatest simulation
     * result to the given container.
     */
    private AbstractState rollOut(AbstractState sState) {
        return sState.randomSim();
    }

    /**
     * Randomly select a child node
     */
    private AbstractMCTSNode randomSelectChildAndUpdateState(AbstractMCTSNode node, AbstractState sState) {
        if (node.isLeaf()) {
            throw new RuntimeException("This node has no child!");
        }
        AbstractMCTSNode selectedChild = node.randomChild();
        sState.update(selectedChild.choices);
        return selectedChild;
    }

    /**
     * Do the selection all the way from root to a leaf node and update state and records accordingly
     */
    private AbstractMCTSNode doSelectionPhase(AbstractState sState, List<AbstractMCTSNode> visited) {
        AbstractMCTSNode cur = rootMCTSNode;
        visited.add(cur);
        while (cur.isNotLeaf()) {
            cur = cur.select();
            // after selection, update the state and add current mcts node to visited
            sState.update(cur.choices);
            visited.add(cur);
        }
        return cur;
    }
}
