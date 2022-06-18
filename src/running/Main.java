package running;

import agent.*;
import graphic.EnvironmentDisplayer;
import world.Environment;
import static running.Default.*;

public class Main {
    static int naiveTotal = 0;
    static int mctsTotal = 0;

    public static void main(String[] args) {
        EnvironmentDisplayer displayer = new EnvironmentDisplayer();
        Default.def_goals = Default.genGoals(def_map_size, def_num_goals, def_initial_Position, rm, 8);
        Environment defEnv = new Environment(def_map_size, def_recharge_position, def_act_consumption,  def_goals);
        AbstractAgent mctsAgent = new MCTSAgent(def_initial_Position, def_recharge_position, def_max_capacity, def_act_consumption);

        boolean running = true;
        defEnv.setAgent(mctsAgent);
        while (running) {
            running = defEnv.run();
            displayer.display(defEnv);
        }

        defEnv = new Environment(def_map_size, def_recharge_position, def_act_consumption,  def_goals);
        AbstractAgent testAgent = new ProactiveFIFOAgent(def_initial_Position, def_recharge_position, def_max_capacity, def_act_consumption);
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
