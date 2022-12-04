package agent;

import gpt.*;
import generator.*;
import machine.*;

import java.util.*;

public class FIFOAgent extends AbstractAgent {

    public FIFOAgent(List<GoalNode> goals, int maxCapacity) {
        super(goals, maxCapacity);
    }

    public FIFOAgent(int maxCapacity) {
        super(maxCapacity);
    }

    @Override
    public boolean reason() {
        // First update intentions.
        intentionUpdate();
        MarsRoverGenerator gen = new MarsRoverGenerator();
        // Then adopt new intentions according to last automata transition.
        List<Automaton> tempAutos = new ArrayList<>(automata);
        outer:
        for (Automaton auto : tempAutos) {
            if (!auto.isTransitionSucceed()) {
                Literal targetLiteral = auto.getTransitionLiteral(auto.getCurState(), auto.getCurState());
                // This is achievement goal type.
                GoalNode newGoal = gen.generate(targetLiteral, bb);
                inner:
                for (Tree intention : intentions) {
                    // @Incomplete: currently, we say two goals are equal when they have the same name.
                    if (newGoal.getName().equals(intention.getTlg().getName())) {
                        continue outer;
                    }
                }
                adoptGoal(newGoal);
            }
        }

        if (intentions.size() == 0) {
            System.out.println("has no intention");
            return false;
        }
        // Select the first intention to progress.
        Tree firstIntention = intentions.get(0);
        choices = getChoices(0, firstIntention.getCurrentStep());
        return true;
    }

    /**
     * For FIFO, just get a single sequence of choices that can be applied given the current
     * step (and intentionIndex).
     */
    private List<Choice> getChoices(int intentionIndex, TreeNode currentStep) {
        List<Choice> choices = new ArrayList<>();
        if (currentStep instanceof ActionNode) {
            choices.add(new Choice(intentionIndex));
        } else if (currentStep instanceof GoalNode) {
            GoalNode goal = (GoalNode) currentStep;
            int planChoice;
            for (int i = 0; i < goal.getPlans().size(); i++) {
                PlanNode plan = goal.getPlans().get(i);
                List<Literal> context = plan.getPrec();
                // If we found an applicable plan, choose it.
                if (bb.eval(context)) {
                    planChoice = i;
                    choices.add(new Choice(intentionIndex, planChoice));
                    currentStep = plan.getBody().get(0);
                    choices.addAll(getChoices(intentionIndex, currentStep));
                    // @Note: remember to break once we found an applicable plan.
                    break;
                }
            }
        }
        return choices;
    }
}
