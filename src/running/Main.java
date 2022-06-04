package running;

import graphic.EnvironmentDisplayer;
import world.Environment;
import static running.Default.*;

public class Main {
    static int defEnvTotal = 0;
    static int mctsEnvTotal = 0;

    public static void main(String[] args) {
        EnvironmentDisplayer displayer = new EnvironmentDisplayer();
        Default.def_goals = Default.randomGenerateTargetPositions(def_map_size, def_num_goals, def_initial_Position);
        Environment defEnv = Default.getNewDefEnv();
        Environment mctsEnv = Default.getNewMctsEnv();

        boolean running = true;
        while (running) {
            running = mctsEnv.run();
            displayer.display(mctsEnv);
        }
        running = true;
        while (running) {
            running = defEnv.run();
            displayer.display(defEnv);
        }

        defEnvTotal += defEnv.getAgent().getTotalFuelConsumption();
        mctsEnvTotal += mctsEnv.getAgent().getTotalFuelConsumption();
        System.out.println("default env resource consumption: " + defEnv.getAgent().getTotalFuelConsumption());
        System.out.println("mcts env resource consumption: " + mctsEnv.getAgent().getTotalFuelConsumption());
    }
}
