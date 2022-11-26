package gpt;

import java.util.*;

public class Tree {
    /**
     * The top-level goal of this tree.
     */
    GoalNode tlg;
    ArrayList<GoalNode> backtrackList;
    TreeNode currentStep;

    // How do we consider a top-level goal is achieved? It is determinded by the agent itself: if
    // the agent estimates that the goal condition can be infered by its belief base, the goal is
    // regarded as achieved.
    // After every execution step, the agent checks whether there are any goals achieved (iterate the
    // goal base).
    public Tree(GoalNode tlg) {
        this.tlg = tlg;
        backtrackList = new ArrayList<>();
        currentStep = tlg;
    }

    public GoalNode getTlg() {
        return tlg;
    }

    public TreeNode getCurrentStep() {
        return this.currentStep;
    }

    public void setCurrentStep(TreeNode node) {
        this.currentStep = node;
    }

    public ActionNode progress() {
        if (currentStep == null) {
            // @Smell: Maybe throwing an exception is a better choice?
            System.out.println("Warning: unable to progress, because current step is null!");
        }
        if (!(currentStep instanceof ActionNode)) {
            throw new RuntimeException("Current step is not an action, cannot be progressed in this function!");
        }

        ActionNode act = (ActionNode) currentStep;
        return act;
    }

    public PlanNode progress(int index) {
        if (currentStep == null) {
            throw new RuntimeException("Unable to progress, because current step is null");
        }
        if (!(currentStep instanceof GoalNode)) {
            throw new RuntimeException("Current step is not a goal, cannot be progressed in this function!");
        }

        GoalNode goal = (GoalNode) currentStep;
        PlanNode selected = goal.getPlans().get(index);
        return null;
    }
}
