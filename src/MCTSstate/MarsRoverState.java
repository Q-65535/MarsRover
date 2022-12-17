package MCTSstate;

import agent.*;
import gpt.*;
import world.*;

import java.util.*;


public class MarsRoverState extends AbstractState {
    SimEnvironment simEnv;
    PuppetAgent simAgent;

    public MarsRoverState(SimEnvironment simEnv) {
	// @Smell: clone shouldn't be placed here...
        this.simEnv = simEnv.clone();
        if (!(simEnv.getAgent() instanceof PuppetAgent)) {
            throw new RuntimeException("Only puppet agent can be added to state");
        }
        simAgent = this.simEnv.getAgent();
    }

    @Override
    public MarsRoverState clone() {
        return new MarsRoverState(this.simEnv);
    }

    @Override
    public void update(List<Choice> choices) {
        simAgent.setChoices(choices);
        simEnv.run();
    }

    @Override
    public double evaluateState() {
        double consumptionEval = 1.0 / (simAgent.getTotalFuelConsumption() + 1.0);
        return simAgent.getVal() + consumptionEval;
    }

    public AbstractState randomSim() {
        MarsRoverState cloneState = this.clone();
        SimEnvironment cloneEnv = cloneState.simEnv;

        boolean running = true;
        while (running) {
            running = cloneEnv.run();
        }
        return cloneState;
    }

    @Override
    public List<List<Choice>> getPossibleNextCs() {
		// Since there are only 4 directions, the choices that results in the same result are
		// just ignored.
		Set<ActionNode> acts = new HashSet<>();

        List<Tree> intentions = simAgent.getIntentions();
        List<List<Choice>> paths = new ArrayList<>();
        for (int i = 0; i < intentions.size(); i++) {
            Tree intention = intentions.get(i);
            // We don't do expansion for suspended intentions.
            if (intention.isSuspend()) continue;
            TreeNode curStep = intention.getCurrentStep();
            if (curStep instanceof GoalNode) {
                GoalNode goal = (GoalNode) curStep;
                ArrayList<ArrayList<Integer>> planIndexPaths = getPosChoices(goal, acts);
                for (ArrayList<Integer> planIndexPath : planIndexPaths) {
                    List<Choice> choicePath = new ArrayList<>();
                    for (int planIndex : planIndexPath) {
                        Choice c = new Choice(i, planIndex);
                        choicePath.add(c);
                    }
                    // Add the last action choice.
                    Choice ac = new Choice(i);
                    choicePath.add(ac);
                    paths.add(choicePath);
                }

                // If it is an action node, just add one choice to the list.
            } else if (curStep instanceof ActionNode) {
                ActionNode act = (ActionNode) curStep;
				// We don't add repeated actions for performance consideration.
				if (acts.contains(act)) {
					continue;
				}
                acts.add(act);
                // If all 4 directions are included, immediately return
                if (acts.size() > 4) {
                    return paths;
                }
                if (simAgent.eval(act.getPrec())) {
                    List<Choice> choicePath = new ArrayList<>();
                    Choice ac = new Choice(i);
                    choicePath.add(ac);
                    paths.add(choicePath);
                }
            }
        }
        return paths;
    }

	// The acts is the set of actions that already considered.
    private ArrayList<ArrayList<Integer>> getPosChoices(GoalNode sg, Set<ActionNode> acts) {
        // initialise the list
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        // get its associated plans
        List<PlanNode> pls = sg.getPlans();
        // check each plan
        for (int i = 0; i < pls.size(); i++) {
            // get the precondition of the plan
            List<Literal> context = pls.get(i).getPrec();
            // if its precondition holds
            if (simAgent.eval(context)) {
                // get the first step of this plan
                TreeNode first = pls.get(i).getBody().get(0);
                // if the first step is an action
                if (first instanceof ActionNode) {
                    ActionNode act = (ActionNode) first;
					// We don't add repeated actions for performance consideration.
					if (acts.contains(act)) {
						continue;
					}
                    acts.add(act);
                    // If all 4 directions are included, immediately return
                    if (acts.size() > 4) {
                        return result;
                    }
                    // initialise the list
                    ArrayList<Integer> cs = new ArrayList<>();
                    // add the plan choice
                    cs.add(i);
                    result.add(cs);
                }
                // if the first step is a subgoal
                else {
                    // cast it to a subgoal
                    GoalNode g = (GoalNode) first;
                    // get the list of choice lists
                    ArrayList<ArrayList<Integer>> css = getPosChoices(g, acts);
                    // for each of these lists
                    for (ArrayList<Integer> s : css) {
                        ArrayList<Integer> cs = new ArrayList<>();
                        // Add the plan choice
                        cs.add(i);
                        // then add all the choices in the list
                        cs.addAll(s);
                        result.add(cs);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public int getAchievedGoalCount() {
        return simAgent.getAchievedGoalCount();
    }

    @Override
    public List<Choice> getChoicePath() {
	return simAgent.getChoicePath();
    }
}
