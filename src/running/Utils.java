package running;

import agent.AbstractAgent;
import agent.FIFOAgent;
import agent.ProactiveFIFOAgent;
import world.Cell;
import world.Environment;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Utils {
    public static int SEED;
    public static Random rm = new Random(SEED);
    public static final int def_map_size = 70;
    public static final int def_num_goals = 100;
    public static final int def_max_capacity = 280;
    public static final int def_act_consumption = 1;
    public static final Cell middle_Position = new Cell(def_map_size / 2, def_map_size / 2);
    public static final Cell initial_Position = middle_Position;
    public static final Cell def_recharge_position = middle_Position;
    public static final Set<Cell> def_goals = randomGenerateTargetPositions(def_map_size, def_num_goals, initial_Position);

    public static final AbstractAgent FIFOAgent = new FIFOAgent(initial_Position, def_goals, def_recharge_position, def_max_capacity, def_act_consumption);

    public static final AbstractAgent proFIFOAgent = new ProactiveFIFOAgent(initial_Position, def_goals, def_recharge_position, def_max_capacity, def_act_consumption);

    public static final Environment defEnv = new Environment(def_map_size, def_recharge_position, FIFOAgent, def_act_consumption);

    /**
     * given the map size and number of goals to generate, randomly generate a specific number of goal cells
     * @param mapSize the map size
     * @param numOfGoals the number of goals to generate
     * @param except the excepted cell position
     * @return a set of target cells
     */
    public static Set<Cell> randomGenerateTargetPositions(int mapSize, int numOfGoals, Cell except) {
        Set<Cell> res = new TreeSet<>();
        while (res.size() < numOfGoals) {
            int x = rm.nextInt(mapSize);
            int y = rm.nextInt(mapSize);
            Cell cell = new Cell(x, y);

            if (except.equals(cell)) {
                continue;
            }
            res.add(cell);
        }
        return res;
    }
}
