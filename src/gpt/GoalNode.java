package gpt;

import world.MarsRoverModel;

import java.util.*;

public class GoalNode extends TreeNode {

    private List<Literal> goalConds;
    List<PlanNode> plans;
    private GoalNode recursiveGoalPtr;

    public GoalNode(String name, List<Literal> goalConds) {
        super(name);
        this.goalConds = goalConds;
        this.plans = new ArrayList<>();
    }

    public GoalNode(String name, List<Literal> goalConds, List<PlanNode> plans) {
        super(name);
        this.goalConds = goalConds;
        this.plans = plans;
    }

    // Generate recursive goal
    public GoalNode(String name, GoalNode recursiveGoal) {
        super(name);
        setRecursiveGoalPtr(recursiveGoal);
    }

    @Override
    public TreeNode getNext() {
        if (this.next != null) {
            return next;
        } else {
            if (this.parent != null) {
                TreeNode parentPlan = this.getParent();
                TreeNode parentGoal = parentPlan.getParent();
                return parentGoal.getNext();
            } else {
                return null;
            }
        }
    }

    /**
     * Estimate whether a given goal has applicable plans in the given
     * model.
     */
    public boolean hasApplicablePlan(MarsRoverModel model) {
        for (PlanNode p : getPlans()) {
            // Found one applicable plan.
            if (model.eval(p.getPrec())) {
                return true;
            }
        }
        // No applicable plans.
        return false;
    }

    public List<PlanNode> getPlans() {
        return this.plans;
    }

    public List<Literal> getGoalConds() {
        return this.goalConds;
    }

    public void setRecursiveGoalPtr(GoalNode goal) {
        this.recursiveGoalPtr = goal;
    }

    public boolean hasRecursiveGoalPtr() {
        return recursiveGoalPtr == null;
    }

    public GoalNode getRecursiveGoalPtr() {
        return this.recursiveGoalPtr;
    }
}
