package world;

import agent.*;
import gpt.*;
import java.util.*;
import gpt.*;

public class SimEnvironment implements Cloneable {
    PuppetAgent agent;

    public SimEnvironment(PuppetAgent puppet) {
        this.agent = puppet;
    }

    public PuppetAgent getAgent() {
        return agent;
    }

    public boolean run() {
        boolean runnable = false;
        agent.intentionUpdate();
        boolean executable = agent.reason();
        if (executable) {
            runnable = true;
            ActionNode act = agent.execute();
            if (act == null) {
                throw new RuntimeException("The agent says it is executable, but has no action to execute after reasoning");
            }
            // @Note: the agent always success in this simulation env.
            this.agent.exeSuccess();
            agent.virtualSense();
            // Intention update must be executed after sense!
            agent.intentionUpdate();
        }
        return runnable;
    }

    @Override
    public SimEnvironment clone() {
        // clone agent
        PuppetAgent cloneAgent = agent.clone();
        // clone the environment
        return new SimEnvironment(cloneAgent);
    }
}
