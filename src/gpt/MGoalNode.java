package gpt;

import java.util.*;

public class MGoalNode extends GoalNode {

    public MGoalNode(String name, List<Literal> goalConds) {
        super(name, goalConds);
    }

    public MGoalNode(String name, List<Literal> goalConds, List<PlanNode> plans) {
        super(name, goalConds, plans);
    }
}
