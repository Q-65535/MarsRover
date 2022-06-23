package mcts;

import MCTSstate.AbstractState;
import agent.MoveAction;

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
    public List<MoveAction> bestActs;

    /**
     * Best simulation result record
     */
     double bestSimulationResult = 0;

    public MCTSWorkSpace() {
        bestActs = new ArrayList<>();
    }

    public void setRootState(AbstractState rootState) {
        this.rootState = rootState;
    }

    public void setRootMCTSNode(AbstractMCTSNode rootMCTSNode) {
        this.rootMCTSNode = rootMCTSNode;
    }


    /**
     * Get the next action choice from best action list. If no action,return null.
     */
    public MoveAction nextBestAct() {
        if (!hasNextBestAct()) {
            return null;
        }
        return bestActs.remove(0);
    }

    public boolean hasNextBestAct() {
        return !bestActs.isEmpty();
    }

    /**
     * Get the best action choice based on the children evaluation
     */
    public MoveAction bestActBasedOnChildren() {
        AbstractMCTSNode bestChild = bestChild();
        if (bestChild == null) {
            return null;
        }
        return bestChild.act;
    }

    /**
     * Get the child node that has the given action
     */
    private AbstractMCTSNode getChildBasedOnAct(AbstractMCTSNode parent, MoveAction act) {
        for (AbstractMCTSNode child : parent.getChildren()) {
            if (child.act.equals(act)) {
                return child;
            }
        }
        throw new RuntimeException("no child has such action: " + act);
    }

    /**
     * The best child of root node based on the node's value. Notice that this evaluation is not based on UCT value, we
     * don't consider exploration at all.
     */
    public AbstractMCTSNode bestChild() {
        // if the root node cannot be expanded any further
        ArrayList<? extends AbstractMCTSNode> children = rootMCTSNode.getChildren();
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
            // current list of actions during selection and simulation
            List<MoveAction> selectionPhaseActs = new ArrayList<>();
            List<AbstractMCTSNode> visited = new LinkedList<>();
            // this copied state will be iteratively updated
            AbstractState sState = rootState.clone();
            // selection
            AbstractMCTSNode curNode = doSelectionPhase(sState, visited, selectionPhaseActs);
            // expansion
            curNode.expand(sState);
            // random selection and rollout
            if (curNode.hasChildren()) {
                AbstractMCTSNode rollOutNode = randomSelectChildAndUpdateState(curNode, sState);
                // record and update according to the randomly selected node
                visited.add(rollOutNode);
                selectionPhaseActs.add(rollOutNode.act);

                // beta times simulation
                for (int j = 0; j < beta; j++) {
                    // record the actions executed during simulation
                    List<MoveAction> simulationPhaseActs = new ArrayList<>();
                    double simulationVal = rollOut(sState, simulationPhaseActs);
                    // update the best simulation result and action list
                    if (bestSimulationResult < simulationVal) {
                        bestSimulationResult = simulationVal;
                        // update action list
                        ArrayList<MoveAction> allActs = new ArrayList<>();
                        // combine two action lists in selection and simulation phases
                        allActs.addAll(selectionPhaseActs);
                        allActs.addAll(simulationPhaseActs);
                        bestActs = allActs;
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
    private double rollOut(AbstractState sState, List<MoveAction> actContainer) {
        return sState.randomSim(actContainer).evaluateState();
    }

    /**
     * Randomly select a child node
     */
    private AbstractMCTSNode randomSelectChildAndUpdateState(AbstractMCTSNode node, AbstractState sState) {
        if (node.isLeaf()) {
            throw new RuntimeException("This node has no child!");
        }
        AbstractMCTSNode selectedChild = node.randomChild();
        sState.exeAct(selectedChild.act);
        return selectedChild;
    }

    /**
     * Do the selection all the way from root to a leaf node and update state and records accordingly
     */
    private AbstractMCTSNode doSelectionPhase(AbstractState sState, List<AbstractMCTSNode> visited, List<MoveAction> currentActs) {
        AbstractMCTSNode cur = rootMCTSNode;
        visited.add(cur);
        while (cur.isNotLeaf()) {
            cur = cur.select();
            // after selection, update the state and add current mcts node to visited
            sState.exeAct(cur.act);
            visited.add(cur);
            // and add the action to the executed action list
            currentActs.add(cur.act);
        }
        return cur;
    }

}
