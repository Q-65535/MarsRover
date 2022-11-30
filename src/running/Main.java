package running;

import agent.AbstractAgent;
import agent.FIFOAgent;
import generator.MarsRoverGenerator;
import gpt.*;
import graphic.EnvironmentDisplayer;
import world.Environment;

import java.util.ArrayList;

import static running.Default.*;

public class Main {
    static int naiveTotal = 0;
    static int mctsTotal = 0;

    public static void main(String[] args) {
        EnvironmentDisplayer displayer = new EnvironmentDisplayer();
        Default.def_goals = Default.genGoals(def_map_size, def_num_goals, def_initial_Position, rm, 0);

        // AbstractAgent fifoagent = new FIFOAgent(def_max_capacity);
        AbstractAgent fifoagent = new FIFOAgent(99999);
        MarsRoverGenerator gen = new MarsRoverGenerator();

        // Construct Goals.
        for (int i = 0; i < 10; i++) {
            ArrayList<Literal> ls = new ArrayList<>();
            fifoagent.adoptGoal(gen.generate(rm.nextInt(20), rm.nextInt(20)));
        }

        Environment defEnv = new Environment(fifoagent);

        boolean running = true;
        defEnv.setAgent(fifoagent);
        while (running) {
            running = defEnv.run();
            displayer.display(defEnv);
        }

        displayer.close();

        naiveTotal += fifoagent.getTotalFuelConsumption();
        mctsTotal += fifoagent.getTotalFuelConsumption();
        System.out.println("default env resource consumption: " + fifoagent.getTotalFuelConsumption());
        System.out.println("MCTS env resource consumption: " + fifoagent.getTotalFuelConsumption());
    }
}
