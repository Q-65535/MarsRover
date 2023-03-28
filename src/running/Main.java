package running;

import agent.*;
import graphic.EnvironmentDisplayer;
import world.*;
import static running.Default.*;
import java.util.*;

public class Main {
    static int naiveTotal = 0;
    static int mctsTotal = 0;

    public static void main(String[] args) {
        EnvironmentDisplayer displayer = new EnvironmentDisplayer();
        Default.def_goals = Default.genGoals(def_map_size, def_num_goals, def_initial_Position, rm);
        Environment defEnv = new Environment(def_goals);
        AbstractAgent mctsAgent = new MCTSAgent(def_max_capacity);

        boolean running = true;
//        defEnv.setAgent(mctsAgent);
//        while (running) {
//            running = defEnv.run();
//            displayer.display(defEnv);
//        }

        defEnv = new Environment(def_goals);
        AbstractAgent testAgent = new FIFOAgent(def_max_capacity);
		// testAgent.setNormLands(genShapeCollection());
		testAgent.setNormLands(genMiddleLineShape(def_map_size));

        running = true;
        defEnv.setAgent(testAgent);
        while (running) {
            running = defEnv.run();
            displayer.display(defEnv);
        }
        displayer.close();

        naiveTotal += testAgent.getTotalFuelConsumption();
        mctsTotal += mctsAgent.getTotalFuelConsumption();
        System.out.println("default env resource consumption: " + testAgent.getTotalFuelConsumption());
        System.out.println("MCTS env resource consumption: " + mctsAgent.getTotalFuelConsumption());
    }
}
