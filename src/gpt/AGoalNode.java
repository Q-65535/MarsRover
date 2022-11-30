package gpt;

import java.util.*;

public class AGoalNode extends GoalNode {

    public AGoalNode(String name, List<Literal> goalConds) {
        super(name, goalConds);
        this.plans = new ArrayList<>();
    }

    public AGoalNode(String name, GoalNode recursiveGoal) {
        super(name, recursiveGoal);
    }

    public AGoalNode(String name, List<PlanNode> planNodes, List<Literal> goalConds) {
        super(name, goalConds);
        if (planNodes == null) {
            throw new RuntimeException("plan is null.");
        }
        this.plans = planNodes;
        for (int i = 0; i <this.plans.size(); i++) {
            this.plans.get(i).setParent(this);
        }
        if (goalConds == null) {
            throw new RuntimeException("goal condition is null.");
        }
    }

    public void addPlan(PlanNode plan) {
        this.plans.add(plan);
    }

    public void addPlans(List<PlanNode> plans) {
        for (PlanNode plan : plans) {
            this.plans.add(plan);
        }
    }

}
