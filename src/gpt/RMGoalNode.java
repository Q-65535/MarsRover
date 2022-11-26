package gpt;

import java.util.*;

public class RMGoalNode extends MGoalNode {

    public RMGoalNode(String name, List<Literal> goalConds) {
        super(name, goalConds);
    }

    public RMGoalNode(String name, List<Literal> goalConds, List<PlanNode> plans) {
        super(name, goalConds, plans);
    }
}
