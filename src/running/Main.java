package running;

import agent.AbstractAgent;
import graphic.EnvironmentDisplayer;
import world.Environment;
import static running.Utils.*;

public class Main {
    static int defEnvTotal = 0;
    static int mctsEnvTotal = 0;

    public static void main(String[] args) {
        EnvironmentDisplayer displayer = new EnvironmentDisplayer();
        Utils.def_goals = Utils.randomGenerateTargetPositions(def_map_size, def_num_goals, initial_Position);
        Environment defEnv = Utils.getNewDefEnv();
        Environment mctsEnv = Utils.getNewMctsEnv();

        boolean running = true;
        while (running) {
            running = mctsEnv.run();
//            displayer.display(mctsEnv);
        }
        running = true;
        while (running) {
            running = defEnv.run();
//            displayer.display(defEnv);
        }

        defEnvTotal += defEnv.getAgent().getTotalFuelConsumption();
        mctsEnvTotal += mctsEnv.getAgent().getTotalFuelConsumption();
        System.out.println("default env resource consumption: " + defEnv.getAgent().getTotalFuelConsumption());
        System.out.println("mcts env resource consumption: " + mctsEnv.getAgent().getTotalFuelConsumption());
    }
}
