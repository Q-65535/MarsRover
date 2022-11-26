package gpt;

import java.util.*;

public class PMGoalNode extends MGoalNode{

    public PMGoalNode(String name, List<Literal> goalConds) {
        super(name, goalConds);
    }

    public PMGoalNode(String name, List<Literal> goalConds, List<PlanNode> plans) {
        super(name, goalConds, plans);
    }

}
