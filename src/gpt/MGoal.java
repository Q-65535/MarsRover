package gpt;

public class MGoal extends Goal {
    // Reactive plans
    private final PlanNode[] reaPlans;
    // Proactive plans
    private final PlanNode[] proPlans;

    public MGoal(String name, Literal[] goalConds) {
        super(name, goalConds);
        this.reaPlans = new PlanNode[0];
        this.proPlans = new PlanNode[0];
    }

    public MGoal(String name, Literal[] goalConds, PlanNode[] reaPlans) {
        super(name, goalConds);
        this.reaPlans = reaPlans;
        this.proPlans = new PlanNode[0];
    }

    public MGoal(String name, Literal[] goalConds, PlanNode[] reaPlans, PlanNode[] proPlans) {
        super(name, goalConds);
        this.reaPlans = reaPlans;
        this.proPlans = proPlans;
    }

    public PlanNode[] getReaPlans() {
        return this.reaPlans;
    }

    public PlanNode[] getProPlans() {
        return this.proPlans;
    }

}
