package gpt;

public class AGoal extends Goal {
    private final PlanNode[] plans;

    public AGoal(String name, Literal[] goalConds) {
        super(name, goalConds);
        this.plans = new PlanNode[0];
    }

    public AGoal(String name, PlanNode[] plannodes, Literal[] goalConds) {
        super(name, goalConds);
        if (plannodes == null) {
            throw new RuntimeException("plan is null.");
        }
        this.plans = plannodes;
        for (int i = 0; i <this.plans.length; i++) {
            this.plans[i].setParent(this);
        }
        if (goalConds == null) {
            throw new RuntimeException("goal condition is null.");
        }
    }

    public PlanNode[] getPlans() {
        return this.plans;
    }
}
