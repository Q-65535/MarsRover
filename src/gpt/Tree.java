package gpt;

import java.util.*;

import world.*;


// @Idea: Maybe we can add state property to intention. When the higher
public class Tree {
    enum Status {
        ACTIVE,
        SUSPEND
    }

    /**
     * The top-level goal of this tree.
     */
    private final GoalNode tlg;
    public final int priority;
    Status status;
    ArrayList<GoalNode> backtrackList;
    TreeNode currentStep;
    boolean isAchieved;

    // How do we consider a top-level goal is achieved? It is determinded by the agent itself: if
    // the agent estimates that the goal condition can be infered by its belief base, the goal is
    // regarded as achieved.
    // After every execution step, the agent checks whether there are any goals achieved (iterate the
    // goal base).
    public Tree(GoalNode tlg) {
        // The default priority of a new intention is 0;
        this(tlg, 0);
    }

    public Tree(GoalNode tlg, int priority) {
        this.tlg = tlg;
        this.priority = priority;
        backtrackList = new ArrayList<>();
        currentStep = tlg;
    }

    public GoalNode getTlg() {
        return tlg;
    }

    public void active() {
        this.status = Status.ACTIVE;
    }

    public void suspend() {
        this.status = Status.SUSPEND;
    }

    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }

    public boolean isSuspend() {
        return this.status == Status.SUSPEND;
    }

    public TreeNode getCurrentStep() {
        return this.currentStep;
    }

    public void setCurrentStep(TreeNode node) {
        this.currentStep = node;
    }

    public List<GoalNode> getBackTrackList() {
        return this.backtrackList;
    }

    // Estimate whether this intention is progressible based on the given model.
    public boolean isNotProgressible(MarsRoverModel model) {
        // If current step is tlg.
        if (currentStep instanceof GoalNode && backtrackList.isEmpty()) {
            GoalNode goal = (GoalNode) currentStep;
            return !goal.hasApplicablePlan(model);
        }
        // Other cases, it is still possible to progress (but not definite).
        return false;
    }


    public ActionNode progress() {
        if (currentStep == null) {
            throw new RuntimeException("Warning: unable to progress, because current step is null!");
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
        backtrackList.add(goal);
        PlanNode selectedPlan = goal.getPlans().get(index);
        // Set currentStep to the first step in the selected plan body.
        setCurrentStep(selectedPlan.getBody().get(0));
        return selectedPlan;
    }

    public void success() {
        if (currentStep instanceof ActionNode) {
            // cast it to an action
            ActionNode act = (ActionNode) currentStep;
            // get the next step of this goal-plan tree
            TreeNode next = act.getNext();
            // a while loop to update the next step, if this action is the last action in a plan
            while (next == null) {

                // get the latest goal
                GoalNode g = backtrackList.remove(backtrackList.size() - 1);
                // if g is the top-level goal
                if (backtrackList.size() == 0) {
                    isAchieved = true;
                }
                // otherwise, if it is the last action in a plan that is not used to achieve the top-level goal
                else {
                    // we set the next step to the next step of the subgoal
                    next = g.getNext();
                }
            }
            // set the current step to the next step
            currentStep = next;
        } else {
            System.out.println("gpt success failed: current step is not an action!");
            System.exit(0);
        }
    }

    /**
     * gpt pregression fail, the agent needs the model to estimate whether the latest
     * goal has applicable plans. If no applicable plans, fail() propogates to higher
     * level.
     */
    public void fail(MarsRoverModel model) {
        // if the current step is not the top-level goal
        if (backtrackList.size() > 0) {
            System.out.println("goal-plan tree progress failure (backtrack list size is " + backtrackList.size() + ")");

            // get the latest goal
            GoalNode goal = backtrackList.remove(backtrackList.size() - 1);
            System.out.println("now, current step is set to goal: " + goal.getName());
            // a boolean value indicating if there are other plans to achieve this goal
            boolean available = goal.hasApplicablePlan(model);
            currentStep = goal;
            // if there are still plans haven't been tried
            if (!available) {
                fail(model);
            }
        }
    }
}
