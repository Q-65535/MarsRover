package mcts;

import MCTSstate.AbstractState;
import agent.MoveAction;
import running.Default;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MCTSWorkSpace {
    Random rm = new Random(Default.SEED);
    /**
     * The number of complete selections
     */
    int beta;
    /**
     * The number of simulations for each selected leaf node
     */
    int alpha;
    final double epsilon = 1e-6;

    /**
     * The state of the root node
     */
     AbstractState rootState;

     AbstractMCTSNode rootMCTSNode;

    /**
     * action sequences lead to the best evaluation score
     */
    List<MoveAction> bestActs;

    /**
     * Best simulation result
     */
     double bResult;

    public MCTSWorkSpace(AbstractState rootState, AbstractMCTSNode rootMCTSNode) {
        this.rootState = rootState;
        this.rootMCTSNode = rootMCTSNode;
        bResult = Double.NEGATIVE_INFINITY;
        bestActs = new ArrayList<>();
    }

    /**
     * Get the best choices based on the MCTS process
     */
    public MoveAction bestChoice() {
        // if no choice can be made, return null
        if (!rootMCTSNode.hasChildren()) {
            return null;
        }

        ArrayList<? extends AbstractMCTSNode> children = rootMCTSNode.getChildren();
        double maxUCT = children.get(0).calUCT();
        AbstractMCTSNode bestChild = children.get(0);
        for (AbstractMCTSNode child : children) {
            if (child.calUCT() > maxUCT) {
                maxUCT = child.calUCT();
                bestChild = child;
            }
        }
        return bestChild.act;
    }

    /**
     * Do the MCTS process
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
                AbstractMCTSNode rollOutNode = randomSelectChild(curNode, sState);
                for (int j = 0; j < beta; j++) {
                    double simulationVal = rollOut(sState, rollOutNode);
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

    private double rollOut(AbstractState sState, AbstractMCTSNode rollOutNode) {
        return rollOutNode.rollOut(sState);
    }

    /**
     * Randomly select a child node
     */
    private AbstractMCTSNode randomSelectChild(AbstractMCTSNode node, AbstractState sState) {
        if (node.isLeaf()) {
            throw new RuntimeException("This node has no child!");
        }
        return node.randomChild();
    }

    /**
     * Given the state, select a leaf node, and update visited list
     * @param sState
     * @param visited
     * @return
     */
    private AbstractMCTSNode doSelectionPhase(AbstractState sState, List<AbstractMCTSNode> visited) {
        AbstractMCTSNode cur = rootMCTSNode;
        visited.add(cur);
        while (cur.isNotLeaf()) {
            cur = cur.select();
            // after selection, update the state and add current mcts node to visited
            sState.exeAct(cur.act);
            visited.add(cur);
        }
        return cur;
    }

    /**
     * get the action path that go from root node to the best leaf node
     */
    public List<MoveAction> getBestActPath() {
        ArrayList<MoveAction> actPath = new ArrayList<>();
        AbstractMCTSNode cur = rootMCTSNode;
        while (cur.isNotLeaf()) {
            cur = cur.exploitSelect();
            actPath.add(cur.act);
        }
        return actPath;
    }
}
