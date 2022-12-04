package generator;

import gpt.*;
import world.*;
import agent.Direction;

import java.util.*;

import static agent.Direction.*;
import static running.Default.*;

public class MarsRoverGenerator {


    public GoalNode generate(Literal targetLiteral, MarsRoverModel model) {
	// If the agent wants to rechrage.
	if (targetLiteral.getFormula() instanceof BatteryAbove) {
	    return generate(def_recharge_position.x, def_recharge_position.y);
	}
	// If it is a specific position to go
	if (targetLiteral.getFormula() instanceof At) {
	    At at = (At) targetLiteral.getFormula();
	    Position targetPosition = at.insTerm1(model);
	    return generate(targetPosition.x, targetPosition.y);
	}
	return null;
    }


    public GoalNode generate(int a, int b) {
        // create a new goal node.
        Position targetPosition = new Position(a, b);
        Formula at = new At(targetPosition);
        Literal l = new Literal(at, true);
        ArrayList gcs = new ArrayList<>();
        gcs.add(l);
        AGoalNode goal = new AGoalNode("at("+a+","+b+")", gcs);

        PlanNode upPlan = genPlan(goal, targetPosition, UP);
        PlanNode downPlan = genPlan(goal, targetPosition, DOWN);
        PlanNode leftPlan = genPlan(goal, targetPosition, LEFT);
        PlanNode rightPlan = genPlan(goal, targetPosition, RIGHT);
        goal.addPlan(upPlan);
        goal.addPlan(downPlan);
        goal.addPlan(leftPlan);
        goal.addPlan(rightPlan);

        return goal;
    }


    private PlanNode genPlan(GoalNode goal, Position targetPosition, Direction direction) {
        // A term can be a function or an instance. If it is a function,
        // we first evaluate it based on a given model.
        PositionFunction agentPosition = new GetAgentPosition();

        Formula preFormula = null;
        ActionNode act = null;
        String planName = "goto" + targetPosition;
        switch (direction) {
            case UP:
                // We generate precondition according to the given direction.
                preFormula = new IsUp(targetPosition, agentPosition);
                break;
            case DOWN:
                preFormula = new IsDown(targetPosition, agentPosition);
                break;
            case LEFT:
                preFormula = new IsLeft(targetPosition, agentPosition);
                break;
            case RIGHT:
                preFormula = new IsRight(targetPosition, agentPosition);
                break;
        }


        act = genAction(direction);
        Literal l = new Literal(preFormula, true);
        List<Literal> prec = new ArrayList<>();
        prec.add(l);

        // Plan body
        // @Note: the subgoal is recursive.
        GoalNode subgoal = goal;
        List<TreeNode> body = new ArrayList<>();
        body.add(act);
        body.add(subgoal);

        PlanNode plan = new PlanNode(planName, prec, body);
        return plan;
    }

    private ActionNode genAction(Direction direction) {
        // The precondition requrires the agent has battery power.
        Formula preFormula = new BatteryAbove(0);
        List<Literal> prec = formulaToConds(preFormula);

        // The post condition
        PositionFunction agentPosition = new GetAgentPosition();
        PositionFunction positionTerm = null;
        // The action name
        String actionName = null;
        switch (direction) {
            case UP:
                // We generate postcondition according to the given direction.
                positionTerm = new GetDiffPosition(agentPosition, 0, 1);
                actionName = "moveUp";
                break;
            case DOWN:
                positionTerm = new GetDiffPosition(agentPosition, 0, -1);
                actionName = "moveDown";
                break;
            case LEFT:
                positionTerm = new GetDiffPosition(agentPosition, -1, 0);
                actionName = "moveLeft";
                break;
            case RIGHT:
                positionTerm = new GetDiffPosition(agentPosition, 1, 0);
                actionName = "moveRight";
                break;
        }
        Formula position = new At(positionTerm);
        NumTerm currentBattery = new GetBattery();
        NumTerm nextBattery = new GetDiffBattery(currentBattery, -1);
        Formula battery = new BatteryEquals(nextBattery);

        List<Literal> post = new ArrayList<>();
        post.add(formulaToLiteral(position));
        post.add(formulaToLiteral(battery));

        return new ActionNode(actionName, prec, post);

    }

    private Literal formulaToLiteral(Formula formula) {
        return new Literal(formula, true);
    }

    private List<Literal> formulaToConds(Formula formula) {
        Literal l = new Literal(formula, true);
        List<Literal> ls = new ArrayList<>();
        ls.add(l);
        return ls;
    }
}
