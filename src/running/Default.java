package running;

import agent.*;
import world.Cell;
import world.Environment;
import world.Norm;

import java.util.*;

public class Default {
    public static int SEED = 1;
    public static Random rm = new Random(SEED);
    public static Random goalGenerateRM = new Random(SEED);
    public static final int def_map_size = 20;
    public static final int def_num_goals = 15;
    public static final int def_max_capacity = def_map_size * 2;
    public static final int def_act_consumption = 1;
    public static final Cell middle_Position = new Cell(def_map_size / 2, def_map_size / 2);
    public static final Cell def_initial_Position = middle_Position;
    public static final Cell def_recharge_position = middle_Position;

    public static Set<Cell> def_goals;

    public static AbstractAgent genNewDefFIFOAgent() {
        return new FIFOAgent(def_initial_Position, cloneCellSet(def_goals), def_recharge_position, def_max_capacity, def_act_consumption);
    }
    public static AbstractAgent genNewDefProFIFOAgent() {
        return new ProactiveFIFOAgent(def_initial_Position, cloneCellSet(def_goals), def_recharge_position, def_max_capacity, def_act_consumption);
    }

    public static AbstractAgent genNewDefGreedyAgent() {
        return new GreedyAgent(def_initial_Position, cloneCellSet(def_goals), def_recharge_position, def_max_capacity, def_act_consumption);
    }

    public static AbstractAgent genNewDefMctsAgent() {
        return new MCTSAgent(def_initial_Position, cloneCellSet(def_goals), def_recharge_position, def_max_capacity, def_act_consumption);
    }

    public static AbstractAgent genNewDefSPMctsAgent() {
        return new SPMCTSAgent(def_initial_Position, cloneCellSet(def_goals), def_recharge_position, def_max_capacity, def_act_consumption);
    }

    public static Environment getNewDefEnv() {
        return new Environment(def_map_size, def_recharge_position, def_act_consumption);
    }

    public static MCTSAgent genNewMctsAgentWithTargetsAndCapacity(Set<Cell> targets, int capacity) {
        return new MCTSAgent(def_initial_Position, targets, def_recharge_position, capacity, def_act_consumption);
    }

    public static ProactiveFIFOAgent genNewProFIFOAgentWithTargetsAndCapacity(Set<Cell> targets, int capacity) {
        return new ProactiveFIFOAgent(def_initial_Position, targets, def_recharge_position, capacity, def_act_consumption);
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
            int x = goalGenerateRM.nextInt(mapSize);
            int y = goalGenerateRM.nextInt(mapSize);
            Cell cell = new Cell(x, y);

            if (except.equals(cell)) {
                continue;
            }
            res.add(cell);
        }
        return res;
    }

    /**
     * Randomly generate a set of goal positions with an except location
     * @param mapSize the length of the map
     * @param numOfGoals number of goals to generate
     * @param except the excluded location, this is usually the initial location of agent
     * @param rm random object
     * @return a set of location cells
     */
    public static Set<Cell> randomGenerateTargetPositions(int mapSize, int numOfGoals, Cell except, Random rm) {
        Set<Cell> res = new HashSet<>();
        while (res.size() < numOfGoals) {
            int x = rm.nextInt(mapSize);
            int y = rm.nextInt(mapSize);
            Cell cell = new Cell(x, y);

            if (cell.equals(except)) {
                continue;
            }
            res.add(cell);
        }
        return res;
    }

    public static HashMap<Cell, Norm> randomGenerateNorms(int mapSize, int numOfNorms, int avgPenalty, Cell except, Random rm) {
        HashMap<Cell, Norm> norms = new HashMap<>();
        while (norms.size() < numOfNorms) {
            int x = rm.nextInt(mapSize);
            int y = rm.nextInt(mapSize);
            Cell cell = new Cell(x, y);

            if (cell.equals(except)) {
                continue;
            }
            double penaltyValue = gaussianDistributionWithRange(avgPenalty, 0.2, 2, rm);
            norms.put(cell, new Norm(cell, penaltyValue));
        }
        return norms;
    }

    /**
     * Generate a number according to normal distribution with specified mean, standard deviation and bounds
     */
    private static double gaussianDistributionWithRange(int mean, double stdDeviation, int range, Random rm) {
        // the generated value
        double value = rm.nextGaussian() * stdDeviation + mean;
        // if the value is not in bounds, generate again and again
        while (value < mean - range || value > mean + range) {
            value = rm.nextGaussian() * stdDeviation + mean;
        }
        return value;
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
