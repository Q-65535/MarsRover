package running;

import MCTSstate.AbstractState;
import agent.AbstractAgent;
import agent.FIFOAgent;
import agent.MCTSAgent;
import agent.ProactiveFIFOAgent;
import world.Cell;
import world.Environment;

import java.util.*;

public class Utils {
    public static Random tempRandom = new Random();
    public static int SEED = tempRandom.nextInt();
    public static Random rm = new Random(SEED);
    public static final int def_map_size = 20;
    public static final int def_num_goals = 10;
    public static final int def_max_capacity = def_map_size * 2;
    public static final int def_act_consumption = 1;
    public static final Cell middle_Position = new Cell(def_map_size / 2, def_map_size / 2);
    public static final Cell initial_Position = middle_Position;
    public static final Cell def_recharge_position = middle_Position;

    public static Set<Cell> def_goals;

    public static AbstractAgent getNewDefProFIFOAgent() {
        return new ProactiveFIFOAgent(initial_Position, cloneCellSet(def_goals), def_recharge_position, def_max_capacity, def_act_consumption);
    }

    public static AbstractAgent getNewDefMctsAgent() {
        return new MCTSAgent(initial_Position, cloneCellSet(def_goals), def_recharge_position, def_max_capacity, def_act_consumption);
    }

    public static Environment getNewDefEnv() {
        return new Environment(def_map_size, def_recharge_position, getNewDefProFIFOAgent(), def_act_consumption);
    }

    public static Environment getNewMctsEnv() {
        return new Environment(def_map_size, def_recharge_position, getNewDefMctsAgent(), def_act_consumption);
    }


    /**
     * given the map size and number of goals to generate, randomly generate a specific number of goal cells
     * @param mapSize the map size
     * @param numOfGoals the number of goals to generate
     * @param except the excepted cell position
     * @return a set of target cells
     */
    public static Set<Cell> randomGenerateTargetPositions(int mapSize, int numOfGoals, Cell except) {
        Set<Cell> res = new HashSet<>();
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

    public static Set<Cell> cloneCellSet(Set<Cell> cells) {
        HashSet<Cell> cloneSet = new HashSet<>();
        for (Cell cell : cells) {
            cloneSet.add(cell);
        }
        return cloneSet;
    }

    public static List<Cell> cloneCellList(List<Cell> cells) {
        ArrayList<Cell> cloneList = new ArrayList<>();
        for (Cell cell : cells) {
            cloneList.add(cell);
        }
        return cloneList;
    }
}
