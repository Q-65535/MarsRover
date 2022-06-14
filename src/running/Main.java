package running;

import agent.AbstractAgent;
import graphic.EnvironmentDisplayer;
import world.Environment;
import static running.Default.*;

public class Main {
    static int naiveTotal = 0;
    static int mctsTotal = 0;

    public static void main(String[] args) {
        EnvironmentDisplayer displayer = new EnvironmentDisplayer();
        Default.def_goals = Default.randomGenerateTargetPositions(def_map_size, def_num_goals, def_initial_Position);
        Environment defEnv = Default.getNewDefEnv();
        AbstractAgent mctsAgent = genNewDefMctsAgent();
        AbstractAgent testAgent = genNewDefGreedyAgent();

        boolean running = true;
        defEnv.setAgent(mctsAgent);
        while (running) {
            running = defEnv.run();
            displayer.display(defEnv);
        }
        running = true;
        defEnv.setAgent(testAgent);
        while (running) {
            running = defEnv.run();
            displayer.display(defEnv);
        }

        naiveTotal += testAgent.getTotalFuelConsumption();
        mctsTotal += mctsAgent.getTotalFuelConsumption();
        System.out.println("default env resource consumption: " + testAgent.getTotalFuelConsumption());
        System.out.println("mcts env resource consumption: " + mctsAgent.getTotalFuelConsumption());
    }
}
