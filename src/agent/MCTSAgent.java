package agent;

import MCTSstate.*;
import gpt.*;
import mcts.*;
import machine.*;
import world.SimEnvironment;

import java.util.*;

public class MCTSAgent extends AbstractAgent {
    // Whether the agent uses proactive strategy (The default value is false.).
    public boolean isProactive;
    MCTSWorkSpace ws = new MCTSWorkSpace();


    public MCTSAgent(int maxCapacity) {
        super(maxCapacity);
    }

    public void setProactive() {
	this.isProactive = true;
    }

    @Override
    public boolean reason() {
        SimEnvironment simEnv = constructSimEnvironment();
        AbstractState rootState = constructState(simEnv);
        AbstractMCTSNode rootNode = constructNode();
        ws.setRootState(rootState);
        ws.setRootMCTSNode(rootNode);

        // run the MCTS process
        ws.run(100, 10);
        if (ws.bestChoices.size() > 0) {
            this.choices = ws.popThisCycleChoices();
            return true;
        }
        return false;
    }

    SimEnvironment constructSimEnvironment() {
        PuppetAgent puppet = genPuppet();
        return new SimEnvironment(puppet);
    }

    private PuppetAgent genPuppet() {
        PuppetAgent puppet = new PuppetAgent(this.maxCapacity);
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
        puppet.val = val;
        puppet.totalFuelConsumption = totalFuelConsumption;
        puppet.achievedGoals = new ArrayList<>(achievedGoals);

        return puppet;
    }

    AbstractState constructState(SimEnvironment simEnv) {
        return new MarsRoverState(simEnv);
    }

    AbstractMCTSNode constructNode() {
        return new NaiveNode();
    }
}
