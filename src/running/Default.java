package running;

import world.Cell;
import world.Norm;
import static world.Calculator.*;

import java.util.*;

public class Default {
    public static int SEED = 1;
    public static Random rm = new Random(SEED);
    public static Random goalGenerateRM = new Random(SEED);
    public static final int def_map_size = 20;
    public static final int def_num_goals = 10;
    public static final int def_max_capacity = def_map_size * 2;
    public static final int def_act_consumption = 1;
    public static final Cell middle_Position = new Cell(def_map_size / 2, def_map_size / 2);
    public static final Cell def_initial_Position = middle_Position;
    public static final Cell def_recharge_position = middle_Position;

    public static List<Cell> def_goals;


    /**
     * given the map size and number of goals to generate, randomly generate a specific number of goal cells
     */
    public static List<Cell> genGoals(int mapSize, int numOfGoals, Cell except) {
        List<Cell> res = new ArrayList<>();
        while (res.size() < numOfGoals) {
            int x = goalGenerateRM.nextInt(mapSize);
            int y = goalGenerateRM.nextInt(mapSize);
            Cell cell = new Cell(x, y);

            // if the newly generated goal is at the forbidden position or already added to the list, continue
            if (except.equals(cell) || res.contains(cell)) {
                continue;
            }
            res.add(cell);
        }
        return res;
    }

    /**
     * Randomly generate a set of goal positions with an except location
     */
    public static List<Cell> genGoals(int mapSize, int numOfGoals, Cell except, Random rm) {
        List<Cell> res = new ArrayList<>();
        while (res.size() < numOfGoals) {
            int x = rm.nextInt(mapSize);
            int y = rm.nextInt(mapSize);
            Cell cell = new Cell(x, y);

            if (cell.equals(except) || res.contains(cell)) {
                continue;
            }
            res.add(cell);
        }
        return res;
    }

    /**
     * Generate a list of goals. The distance between any two goals must be equal or greater than the minimum distance
    */
    public static List<Cell> genGoals(int mapSize, int goalCount, Cell except, Random rm, int minDistance) {
        List<Cell> goals = new ArrayList<>();
        while (goals.size() < goalCount) {
            int x = rm.nextInt(mapSize);
            int y = rm.nextInt(mapSize);
            Cell newGoal = new Cell(x, y);

            // make sure no duplicate locations
            if (newGoal.equals(except) || goals.contains(newGoal)) {
                continue;
            }
            // the distance between any two goals shall not be greater than the minimum distance
            if (exceedMinDistance(newGoal, goals, minDistance)) {
                continue;
            }
            goals.add(newGoal);
        }
        return goals;
    }

    private static boolean exceedMinDistance(Cell newGoal, List<Cell> goals, int minDistance) {
        for (Cell goal : goals) {
            int curDistance = calculateDistance(newGoal, goal);
            if (curDistance < minDistance) {
                return true;
            }
        }
        return false;
    }

    public static HashMap<Cell, Norm> genNorms(int mapSize, int numOfNorms, double avgPenalty, Cell except, Random rm) {
        HashMap<Cell, Norm> norms = new HashMap<>();
        while (norms.size() < numOfNorms) {
            int x = rm.nextInt(mapSize);
            int y = rm.nextInt(mapSize);
            Cell cell = new Cell(x, y);
            if (cell.equals(except)) {
                continue;
            }
            double penaltyValue = gaussian(avgPenalty, 0.01, 0.01, rm);
            norms.put(cell, new Norm(cell, penaltyValue));
        }
        return norms;
    }

    /**
     * Generate a number according to normal distribution with specified mean, standard deviation and bounds
     */
    private static double gaussian(double mean, double stdDeviation, double rangeRadius, Random rm) {
        // the generated value
        double value = rm.nextGaussian() * stdDeviation + mean;
        // if the value is not in bounds, generate again and again
        while (value < mean - rangeRadius || value > mean + rangeRadius) {
            value = rm.nextGaussian() * stdDeviation + mean;
        }
        return value;
    }

    public static List<Cell> cloneCells(List<Cell> cells) {
        List<Cell> cloneSet = new ArrayList<>();
        for (Cell cell : cells) {
            cloneSet.add(cell);
        }
        return cloneSet;
    }
}
