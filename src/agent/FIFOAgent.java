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
        // First update intentions.
        intentionUpdate();

        MarsRoverGenerator gen = new MarsRoverGenerator();
        // Then adopt new intentions according to last automata transition.
        List<Automaton> tempAutomata = new ArrayList<>(automata);
        outer:
        for (Automaton auto : tempAutomata) {
            Literal nextTargetLiteral = auto.getNextTargetLiteral();
	    // Some automata has not nextTargetLiteral, e.g., norms.
            if (nextTargetLiteral == null) {
                continue;
            }

            for (int i = 0; i < 3; i++) {

            }
            // Now, we consider the transitions that always success.
            Literal targetLiteral = auto.getNextTargetLiteral();
            // If the target literal is currently satisfied, just ignore.
            if (bb.eval(targetLiteral) == true) {
                continue;
            }

            GoalNode newGoal = gen.generate(targetLiteral, bb);

            // This is achievement goal type.
            for (Tree intention : intentions) {
                // @Incomplete: currently, we say two goals are equal when they have the same name.
                if (newGoal.getName().equals(intention.getTlg().getName())) {
                    continue outer;
                }
            }
            // The priority of the new intention is determined by the automaton.
            adoptGoal(newGoal, auto.priority);
        }

        if (intentions.size() == 0) {
            System.out.printf("Execution stop: the agent has 0 intention.\n");
            return false;
        }


	// Each reasoning cycle, we active all intention and then deactive low priority conflicting
	// intentions.
	activeAllIntentions();
	Tree prioIntention = getPriorIntention();
	suspendConflictingIntentions(prioIntention);

        // Select the first highest priority intention to progress.
        Tree selectedIntention = intentions.get(0);
        for (Tree intention : intentions) {
            // Find the first active intention to progress.
            if (intention.isActive()) {
                selectedIntention = intention;
                break;
            }
        }
        choices = getChoices(0, selectedIntention.getCurrentStep());
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
