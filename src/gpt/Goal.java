package gpt;

import java.util.*;

public class Goal extends TreeNode {

    private final Literal[] goalConds;

    public Goal(String name, Literal[] goalConds) {
        super(name);
        this.goalConds = goalConds;
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

    public Literal[] getGoalConds() {
        return this.goalConds;
    }
}
