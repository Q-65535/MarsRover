package agent;

import generator.StateMachineGenerator;
import running.*;
import world.*;

import static running.Default.*;

import gpt.*;
import machine.*;

import java.util.*;

public abstract class AbstractAgent implements Cloneable {
    public static final int infinite_capacity = Integer.MAX_VALUE;
    public static final double delta = 1e-6;
    Random rm = new Random(Default.SEED);
    // The map size from agent perspective.
    final int mapSize = def_map_size;
    // The agent's belief base.
    MarsRoverModel bb;
    List<Automaton> automata;
    // The goals represented by automata.
    // @Idea: Maybe we can add norms into this? We can call them temporal objects.
    // @Incomplete: Currently, we don't use this field, since we consider automata.
    List<GoalNode> temporalGoals;
    List<MGoalNode> maitGoals;
    List<Tree> intentions;
    // choices in current cycle.
    List<Choice> choices;
    List<AGoalNode> achievedGoals;

    /**
     * The total penalty received
     */
    // @Idea: Maybe we need implement an overall value that is the sum of penalty and reward.
    double penalty;
    HashMap<Position, Norm> norms;
    Position currentPosition;
    // @Refactor: This should be implemented by choices.
    ActionNode currentAct;
    Position prePosition;
    Position rechargePosition;
    int maxCapacity;
    int totalFuelConsumption;
    int rechargeFuelConsumption;
    /**
     * whether the current agent is going to recharge
     */
    boolean isGoToRecharge;
    /**
     * how much fuel will be consumed for each action (agent's perspective)
     */
    int actFuelConsumption;
    /**
     * Whether a goal is achieved. If a goal is achieved, the value is true.
     * The value is turned to false when the agent recharge (This prevents the agent from recharging again
     * and again without achieving any goals.
     *
     * @Smell: init the value in the constructor!!!!!!!
     */
    public boolean isAchieved = false;

    public AbstractAgent(List<GoalNode> goals, int maxCapacity) {
        init();
        this.maxCapacity = maxCapacity;
        this.bb.setAgentFuel(maxCapacity);
        adoptGoals(goals);
    }

    public AbstractAgent(int maxCapacity) {
        init();
        this.maxCapacity = maxCapacity;
        this.bb.setAgentFuel(maxCapacity);
    }

    private void init() {
        // init belief base.
        this.bb = new MarsRoverModel();
        bb.setAgentPosition(def_initial_Position);
        this.prePosition = currentPosition;
        this.actFuelConsumption = def_act_consumption;
        this.rechargePosition = def_initial_Position;

        intentions = new ArrayList<>();
        maitGoals = new ArrayList<>();
        choices = new ArrayList<>();
        achievedGoals = new ArrayList<>();
        automata = new ArrayList<>();
        totalFuelConsumption = 0;
        rechargeFuelConsumption = 0;
        penalty = 0;
    }

    public MarsRoverModel getBB() {
        return this.bb;
    }

    public List<Automaton> getAutomata() {
        return automata;
    }

    // @Incomplete: implement sense method.
    public void sense(Environment env) {
        bb.sync(env.getModel());
        // Given the model transit each automaton.
        for (Automaton auto : automata) {
            auto.transit(bb);
        }
    }

    public void adoptGoals(List<GoalNode> goals) {
        for (GoalNode goal : goals) {
            adoptGoal(goal);
        }
    }

    // Adopt a new achievement goal.
    public void adoptGoal(GoalNode goal) {
        StateMachineGenerator gen = new StateMachineGenerator();
        // Achievement goals are immediately transformed to intentions.
        if (goal instanceof AGoalNode) {
            AGoalNode ag = (AGoalNode) goal;
            // Add to the first of the queue.
            intentions.add(0, new Tree(ag));
            // Add automaton corresponding to the achievement goal.
            // @Test: This way of adding automaton is just for testing.
            Automaton auto = gen.genBasicAchievementAuto(getGoalLiteral(ag));
            automata.add(auto);
        } else if (goal instanceof MGoalNode) {
            MGoalNode mg = (MGoalNode) goal;
            maitGoals.add(mg);
            // Add automaton corresponding to the maintenance goal.
            Automaton auto = gen.genBasicMaitAuto(getGoalLiteral(mg));
            automata.add(auto);
        } else {
            throw new RuntimeException("this goal is not recognized!");
        }
    }

    private Literal getGoalLiteral(GoalNode goal) {
        // Assuming the goal condition only contains one literal.
        return goal.getGoalConds().get(0);
    }

    public int getCurrentFuel() {
        return bb.getAgentFuel();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public List<Tree> getIntentions() {
        return this.intentions;
    }

    public List<GoalNode> getTemporalGoals() {
        return this.temporalGoals;
    }

    public Position getCurrentPosition() {
        return bb.getAgentPosition();
    }

    public int getAchievedGoalCount() {
        return achievedGoals.size();
    }

    public int getTotalFuelConsumption() {
        return totalFuelConsumption;
    }

    // @? how to implement aggregate value function?
    public double getAggregateVal() {
        return 100 * (achievedGoals.size() / (totalFuelConsumption + delta) - getTotalPenalty());
    }

    public int getRechargeFuelConsumption() {
        return rechargeFuelConsumption;
    }


    /**
     * Based on current belief base, we check whether current intentions is achieved or
     * failed. If achieved, we just drop it; if failied (i.e., current goal has no plan choices or
     * current action can't be executed), fail propogates to higher levels.
     */
    public void intentionUpdate() {
        successIntentionUpdate();
        failIntentionUpdate();
    }

    private void successIntentionUpdate() {
        Iterator<Tree> it = intentions.iterator();
        while (it.hasNext()) {
            Tree gpt = it.next();
            GoalNode tlg = gpt.getTlg();
            // If the tlg of current intention is already achieved, drop the intention.
            if (bb.eval(tlg.getGoalConds())) {
                System.out.println("a goal has just achieved by accident!");
                // We only add achievement goals to the achievedGoals list.
                // @? Maybe maintenance goal should be considered?
                if (tlg instanceof AGoalNode) {
                    AGoalNode ag = (AGoalNode) tlg;
                    achievedGoals.add(ag);
                }
                // drop the intention.
                it.remove();
            }
        }
    }

    private void failIntentionUpdate() {
        Iterator<Tree> iterator = intentions.iterator();
        while (iterator.hasNext()) {
            Tree curIntention = iterator.next();
            if (curIntention.getCurrentStep() instanceof ActionNode) {
                ActionNode act = (ActionNode) curIntention.getCurrentStep();
                if (!bb.eval(act.getPrec())) {
                    curIntention.fail(bb);
                }
            }
            if (curIntention.getCurrentStep() instanceof GoalNode) {
                GoalNode goal = (GoalNode) curIntention.getCurrentStep();
                if (!goal.hasApplicablePlan(bb)) {
                    System.out.println("this goal has no applicable plan");
                    curIntention.fail(bb);
                }
            }
            if (curIntention.isProgressible(bb)) {
                break;
                // If this intention is not progressible after preprocessing, remove it.
            } else {
                System.out.println("not progressiable");
                iterator.remove();
            }
        }
    }

    // Agent reasons about what to do. It returns true if it know what to do
    // after reasoning. False if it has no idea about what to do next. This method
    // also alter the agent's internal state (what to do next).
    public abstract boolean reason();


    /**
     * Progress intention and finally return the action ready to be executed.
     */
    public ActionNode execute() {
        // check if there is a decision has been made already. If there is, then execute it
        while (this.choices.size() > 0) {
            // get the immediate choice
            Choice choice = this.choices.get(0);
            // if it is a plan choice
            if (choice.isPlanSelection()) {
                choices.remove(0);
                Tree gpt = this.intentions.get(choice.intentionChoice);
                gpt.progress(choice.planChoice);
            }
            // if the choice is to execute an action
            else if (choice.isActionExecution()) {
                Tree gpt = this.intentions.get(choice.intentionChoice);
                ActionNode act = gpt.progress();
                return act;
            }
        }
        // If no action is returned, null is returned.
        return null;
    }

    /**
     * NOT progress intention, just return the action ready to be executed.
     */
    public ActionNode virtualExecute() {
        // Prepare to virtual progress
        List<Choice> choicesCopy = new ArrayList<>(choices);
        for (Tree intention : intentions) {
            intention.resetVirtualCurrentStep();
        }
        while (choicesCopy.size() > 0) {
            // get the immediate choice
            Choice choice = choicesCopy.get(0);
            // if it is a plan choice
            if (choice.isPlanSelection()) {
                choicesCopy.remove(0);
                Tree gpt = this.intentions.get(choice.intentionChoice);
                gpt.virtualProgress(choice.planChoice);
            }
            // if the choice is to execute an action
            else if (choice.isActionExecution()) {
                Tree gpt = this.intentions.get(choice.intentionChoice);
                ActionNode act = gpt.virtualProgress();
                return act;
            }
        }
        return null;
    }

    // @Note: the last action choice is removed in success method.
    public void exeSuccess() {
        int previousFuel = bb.getAgentFuel();
        if (choices.size() > 1) {
            throw new RuntimeException("success error: current choice is not the last choice!");
        }
        Choice choice = choices.remove(0);
        Tree gpt = intentions.get(choice.intentionChoice);
        ActionNode act = (ActionNode) gpt.getCurrentStep();
        bb.apply(act.getPostc());
        gpt.success();
        // If the tlg is regarded as achieved based on the updated belief base, drop it.
        GoalNode tlg = gpt.getTlg();
        if (bb.eval(tlg.getGoalConds())) {
            // We add achievement goal to the achievedGoals list.
            if (tlg instanceof AGoalNode) {
                AGoalNode ag = (AGoalNode) tlg;
                achievedGoals.add(ag);
            }
            // drop the intention.
            intentions.remove(choice.intentionChoice);

        }

        // After bb is update, when then record the total fuel consumption and recharge info.
        int currentFuel = bb.getAgentFuel();
        updateFuelRecord(previousFuel, currentFuel);

        // Then, recharge.
        if (rechargePosition.equals(bb.getAgentPosition())) {
            bb.setAgentFuel(maxCapacity);
        }
    }

    public void exeFail(MarsRoverModel model) {
        int previousFuel = bb.getAgentFuel();
        if (choices.size() > 1) {
            throw new RuntimeException("fail error: current choice is not the last choice!");
        }
        Choice choice = choices.remove(0);
        Tree gpt = intentions.get(choice.intentionChoice);
        gpt.fail(model);
        // If this intention is not progressible based on current belief base, drop it.
        if (gpt.isProgressible(bb)) {
            intentions.remove(choice.intentionChoice);
        }
    }


    /**
     * Update the totoal fuel consumption according to the fuel level before and
     * after an action execution.
     */
    public void updateFuelRecord(int before, int after) {
        totalFuelConsumption += (before - after);
    }


    public int estimateFuelConsumption(Position from, Position to) {
        int distance = Calculator.calculateDistance(from, to);
        return distance * actFuelConsumption;
    }

    /**
     * if current position is recharge position, recharge the fuel
     */
    public void updateRecharge() {
        if (bb.getAgentPosition().equals(rechargePosition)) {
            bb.setAgentFuel(maxCapacity);
            // isAchieved turned to false when the agent recharge
            isAchieved = false;
        }
    }

    /**
     * update total penalty value according to current position and norms
     */
    public void receivePunish(double penaltyValue) {
        this.penalty += penaltyValue;
        // @Incomplete: We need a learning mechanism in the future?
    }

    public double getTotalPenalty() {
        return penalty;
    }

    public Set<Position> getNormPositions() {
        if (norms == null) {
            return new HashSet<>();
        }
        return norms.keySet();
    }
}
