package agent;

import gpt.*;
import generator.*;
import machine.*;

import java.util.*;

public class FIFOAgent extends AbstractAgent {

    public FIFOAgent(int maxCapacity) {
        super(maxCapacity);
    }

    @Override
    public boolean reason() {
        if (intentions.size() == 0) {
            System.out.printf("Execution stop: the agent has 0 intention.\n");
            return false;
        }

        // Select the first highest priority intention to progress.
        Tree selectedIntention = null;
	int selectedIntentionIndex = -1;
        for (int i = 0; i < intentions.size(); i++) {
	    Tree intention = intentions.get(i);
            // Find the first active intention to progress.
            if (intention.isActive()) {
                selectedIntention = intention;
		selectedIntentionIndex = i;
                break;
            }
        }
	// No active intentions, return false.
	if (selectedIntention == null) {
	    return false;
	}

        choices = getChoices(selectedIntentionIndex, selectedIntention.getCurrentStep());
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
                    // If the current step is goal, recursion applies.
                    choices.addAll(getChoices(intentionIndex, currentStep));
                    // @Note: remember to break once we found an applicable plan.
                    break;
                }
            }
        }
        return choices;
    }
}
