//package MCTSstate;
//
//import agent.BeliefBaseImp;
//import agent.Choice;
//import goalplantree.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BasicState extends AbstractState {
//    private GoalPlanTree[] gpts;
//
//    private BeliefBaseImp beliefs;
//
//    public BasicState(GoalPlanTree[] gpts, BeliefBaseImp beliefs) {
//        GoalPlanTree[] cloneGpts = new GoalPlanTree[gpts.length];
//        for (int i = 0; i < gpts.length; i++) {
//            cloneGpts[i] = gpts[i].clone();
//        }
//        this.gpts = cloneGpts;
//        this.beliefs = beliefs.clone();
//    }
//
//    @Override
//    public void update(List<Choice> choices) {
//        for (Choice c : choices) {
//            update(c);
//        }
//    }
//
//    private void update(Choice c) {
//        // get the chosen intention
//        GoalPlanTree gpt = gpts[c.intentionChoice];
//        // if the choice is plan selection
//        if (c.isPlanSelection() && gpt.getCurrentStep() instanceof GoalNode) {
//            GoalNode goal = (GoalNode) gpt.getCurrentStep();
//            PlanNode[] plans = goal.getPlans();
//            gpt.getBacktrackList().add(goal);
//            // set current step to the first step in the plan body
//            gpt.setCurrentStep(plans[c.planChoice].getPlanbody()[0]);
//            // if the choice is action execution
//        } else if (c.isActionExecution() && gpt.getCurrentStep() instanceof ActionNode) {
//            ActionNode act = (ActionNode) gpt.getCurrentStep();
//            Literal[] postc = act.getPostc();
//            for (Literal l : postc) {
//                beliefs.update(l);
//            }
//            TreeNode nextStep = act.getNext();
//            while (nextStep == null) {
//                // if it is tlg
//                if (gpt.getBacktrackList().isEmpty()) {
//                    break;
//                }
//                GoalNode parentGoal = gpt.getBacktrackList().remove(gpt.getBacktrackList().size() - 1);
//                nextStep = parentGoal.getNext();
//            }
//            // finally set the next step
//            gpt.setCurrentStep(nextStep);
//        }
//    }
//
//    @Override
//    public double evaluateState() {
//        double achieved = 0.0;
//        for (GoalPlanTree gpt : gpts) {
//            if (gpt == null || gpt.getCurrentStep() == null) {
//                achieved++;
//            }
//        }
//        return achieved;
//    }
//
//    @Override
//    public BasicState clone() {
//        BeliefBaseImp cloneBelief = beliefs.clone();
//        //TODO check if the clone operation is correct
//        GoalPlanTree[] cloneGpts = gpts.clone();
//        return new BasicState(cloneGpts, cloneBelief);
//    }
//
//    @Override
//    public AbstractState randomSim() {
//        BasicState cloneState = this.clone();
//        BeliefBaseImp cBelief = cloneState.beliefs;
//        GoalPlanTree[] cGpts = cloneState.gpts;
//        ArrayList<Integer> idxs = new ArrayList<>();
//        for (int i = 0; i < cGpts.length; i++) {
//            if (cGpts[i].getCurrentStep() != null) {
//                idxs.add(i);
//            }
//        }
//
//        intentionloop:
//        while (idxs.size() > 0) {
//            // initiate a list of choices in current iteration
//            ArrayList<Choice> curCs = new ArrayList<>();
//
//            // randomly pick a choice
//            int r = rm.nextInt(idxs.size());
//            // the selected index is removed
//            Integer index = idxs.remove(r);
//            GoalPlanTree gpt = cGpts[index];
//
//            TreeNode currentStep = gpt.getCurrentStep();
//            goalloop:
//            while (currentStep instanceof GoalNode) {
//                GoalNode goal = (GoalNode) currentStep;
//                PlanNode[] pls = goal.getPlans();
//                ArrayList<Integer> planIdxs = new ArrayList<>();
//                // add all plan indices
//                for (int i = 0; i < pls.length; i++) {
//                    planIdxs.add(i);
//                }
//                planloop:
//                while (planIdxs.size() > 0) {
//                    int rIdx = rm.nextInt(planIdxs.size());
//                    Integer planIdx = planIdxs.remove(rIdx);
//                    PlanNode pl = pls[planIdx];
//                    Literal[] prec = pl.getPrec();
//                    if (cBelief.evaluate(prec) == 1) {
//                        currentStep = pl.getPlanbody()[0];
//                        Choice choice = new Choice(index, planIdx);
//                        curCs.add(choice);
//                        continue goalloop;
//                    }
//                }
//                continue intentionloop;
//            }
//
//            // if select an action
//            if (currentStep instanceof ActionNode) {
//                ActionNode act = (ActionNode) currentStep;
//                Literal[] prec = act.getPrec();
//                if (cBelief.evaluate(prec) == 1) {
//                    curCs.add(new Choice(index));
//                    cloneState.update(curCs);
//                    idxs.clear();
//                    for (int i = 0; i < cGpts.length; i++) {
//                        if (cGpts[i] != null && cGpts[i].getCurrentStep() != null) {
//                            idxs.add(i);
//                        }
//                    }
//                    continue intentionloop;
//                }
//                // action can't be executed
//                else {
//                    curCs.clear();
//                    continue intentionloop;
//                }
//            }
//        }
//
//        return cloneState;
//    }
//
//    /**
//     * Get the number of achieved goals in current state
//     */
//    public int getNumOfAchievedGoals() {
//        int ct = 0;
//        for (GoalPlanTree gpt : gpts) {
//            if (gpt.achieved()) {
//                ct++;
//            }
//        }
//        return ct;
//    }
//
//    @Override
//    public ArrayList<ArrayList<Choice>> getPossibleNextCs() {
//        ArrayList<ArrayList<Choice>> ncs = new ArrayList<>();
//
//        for (int i = 0; i < gpts.length; i++) {
//            TreeNode cStep = gpts[i].getCurrentStep();
//
//            if (cStep instanceof GoalNode) {
//                GoalNode sg = (GoalNode) cStep;
//                ArrayList<ArrayList<Integer>> sChoice = getPosChoices(sg);
//                for (ArrayList<Integer> cList : sChoice) {
//                    ArrayList<Choice> cs = new ArrayList<>();
//                    for (Integer l : cList) {
//                        Choice c = new Choice(i, l);
//                        cs.add(c);
//                    }
//                    // add last action choice
//                    cs.add(new Choice(i));
//                    // add the list of choices to result
//                    ncs.add(cs);
//                }
//            } else if (cStep instanceof ActionNode) {
//                ActionNode act = (ActionNode) cStep;
//                Literal[] prec = act.getPrec();
//                if (beliefs.evaluate(prec) == 1) {
//                    ArrayList<Choice> cs = new ArrayList<>();
//                    cs.add(new Choice(i));
//                    ncs.add(cs);
//                }
//            }
//        }
//
//        return ncs;
//    }
//
//    /**
//     * Calculate all possible plan choices paths of a goal until the first action
//     *
//     * @param sg The given goal
//     * @return
//     */
//    private ArrayList<ArrayList<Integer>> getPosChoices(GoalNode sg) {
//        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
//        PlanNode[] pls = sg.getPlans();
//        for (int i = 0; i < pls.length; i++) {
//            Literal[] context = pls[i].getPrec();
//            if (beliefs.evaluate(context) == 1) {
//                TreeNode firstStep = pls[i].getPlanbody()[0];
//                if (firstStep instanceof ActionNode) {
//                    ArrayList<Integer> cs = new ArrayList<>();
//                    cs.add(i);
//                    result.add(cs);
//                } else {
//                    GoalNode g = (GoalNode) firstStep;
//                    ArrayList<ArrayList<Integer>> css = getPosChoices(g);
//                    for (ArrayList<Integer> s : css) {
//                        ArrayList<Integer> cs = new ArrayList<>();
//                        cs.add(i);
//                        cs.addAll(s);
//                        result.add(cs);
//                    }
//                }
//            }
//        }
//        return result;
//    }
//}
