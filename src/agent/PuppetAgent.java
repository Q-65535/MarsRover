package agent;

import java.util.Random;

import gpt.*;

import java.util.*;

import machine.*;

public class PuppetAgent extends AbstractAgent implements Cloneable {
    static Random rm = new Random();
    List<Choice> choicePath;
    int preActiveIntentionSize = 0;
    int randomIndex;

    public PuppetAgent(int maxCapacity) {
        super(maxCapacity);
        choicePath = new ArrayList<>();
    }

    public void setChoices(List<Choice> choices) {
        this.choices = new ArrayList<>(choices);
    }

    public List<Choice> getChoicePath() {
        return choicePath;
    }

    @Override
    public boolean reason() {
        if (intentions.size() == 0) {
            // System.out.printf("Execution stop: the agent has 0 intention.\n");
            return false;
        }

        // If the choices are specified by other function, just return true.
        if (choices.size() > 0) {
            return true;
        }

        List<Integer> activeIntentionIndecies = new ArrayList<>();
        for (int i = 0; i < intentions.size(); i++) {
            Tree intention = intentions.get(i);
            if (intention.isActive()) {
                activeIntentionIndecies.add(i);
            }
        }
        // If the active intention size changes, we then randomly select another intention.
        if (activeIntentionIndecies.size() != preActiveIntentionSize) {
            preActiveIntentionSize = activeIntentionIndecies.size();
            randomIndex = activeIntentionIndecies.get(rm.nextInt(activeIntentionIndecies.size()));
        }

        int intentionIndex = activeIntentionIndecies.get(randomIndex);
        Tree curIntention = intentions.get(intentionIndex);

        if (curIntention == null) {
            return false;
        }

        choices = getChoices(intentionIndex, curIntention.getCurrentStep());
        return true;
    }


    // Here, we randomly choose an applicable plan.
    private List<Choice> getChoices(int intentionIndex, TreeNode currentStep) {
        List<Choice> choices = new ArrayList<>();
        if (currentStep instanceof ActionNode) {
            choices.add(new Choice(intentionIndex));
        } else if (currentStep instanceof GoalNode) {
            GoalNode goal = (GoalNode) currentStep;
            List<Integer> applicablePlanIndecies = new ArrayList<>();
            for (int i = 0; i < goal.getPlans().size(); i++) {
                PlanNode plan = goal.getPlans().get(i);
                List<Literal> context = plan.getPrec();
                // If we found an applicable plan, add it to list.
                if (bb.eval(context)) {
                    applicablePlanIndecies.add(i);
                }
            }
            // Randomly select an applicable plan.
            int planChoice = applicablePlanIndecies.get(rm.nextInt(applicablePlanIndecies.size()));
            choices.add(new Choice(intentionIndex, planChoice));
            PlanNode plan = goal.getPlans().get(planChoice);
            currentStep = plan.getBody().get(0);
            // If the current step is goal, recursion applies.
            choices.addAll(getChoices(intentionIndex, currentStep));
        }
        return choices;
    }

    /**
     * Not actually sense the env, just update automata.
     */
    public void virtualSense() {
        // Given the model transit each automaton.
        Iterator<Automaton> it = automata.iterator();
        while (it.hasNext()) {
            Automaton auto = it.next();
            auto.transit(bb);
            // Each transition apply the value.
            val += auto.getCurState().val;
            if (auto.isFinalTrap()) {
                it.remove();
            }
        }
    }

    /**
     * Override execute method. We want to update choice path when during execution.
     */
    public ActionNode execute() {
        // check if there is a decision has been made already. If there is, then execute it
        while (this.choices.size() > 0) {
            // get the immediate choice
            Choice choice = this.choices.get(0);
            // Add the choice to choice path.
            choicePath.add(choice);

            if (choice.isPlanSelection()) {
                choices.remove(0);
                Tree gpt = this.intentions.get(choice.intentionChoice);
                gpt.progress(choice.planChoice);
            } else if (choice.isActionExecution()) {
                Tree gpt = this.intentions.get(choice.intentionChoice);
                ActionNode act = gpt.progress();
                return act;
            }
        }
        // If no action to execute, null is returned.
        return null;
    }

    @Override
    public PuppetAgent clone() {
        PuppetAgent puppet = new PuppetAgent(maxCapacity);
        puppet.bb = bb.clone();
        // Clone automata.
        for (Automaton auto : automata) {
            puppet.automata.add(auto.clone());
        }
        // Clone intentions.
        for (Tree intention : intentions) {
            puppet.intentions.add(intention.clone());
        }
        // Clone other properties.
        puppet.choicePath = new ArrayList<>(choicePath);
        puppet.val = val;
        puppet.totalFuelConsumption = totalFuelConsumption;
        puppet.achievedGoals = new ArrayList<>(achievedGoals);

        return puppet;
    }
}
