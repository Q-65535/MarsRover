package running;

import generator.StateMachineGenerator;
import gpt.Position;
import machine.Automaton;
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
    public static final Position middle_Position = new Position(def_map_size / 2, def_map_size / 2);
    public static final Position def_initial_Position = middle_Position;
    public static final Position def_recharge_position = middle_Position;

    public static List<Position> def_goals;


    /**
     * given the map size and number of goals to generate, randomly generate a specific number of goal cells
     */
    public static List<Position> genGoals(int mapSize, int numOfGoals, Position except) {
        List<Position> res = new ArrayList<>();
        while (res.size() < numOfGoals) {
            int x = goalGenerateRM.nextInt(mapSize);
            int y = goalGenerateRM.nextInt(mapSize);
            Position position = new Position(x, y);

            // if the newly generated goal is at the forbidden position or already added to the list, continue
            if (except.equals(position) || res.contains(position)) {
                continue;
            }
            res.add(position);
        }
        return res;
    }

    /**
     * Randomly generate a set of goal positions with an except location
     */
    public static List<Automaton> genGoals(int mapSize, int numOfGoals, Position except, Random rm) {
        StateMachineGenerator gen = new StateMachineGenerator();
        List<Automaton> res = new ArrayList<>();
        while (res.size() < numOfGoals) {
            int x = rm.nextInt(mapSize);
            int y = rm.nextInt(mapSize);
            Position position = new Position(x, y);

            if (position.equals(except) || res.contains(position)) {
                continue;
            }

            res.add(gen.genBasicAchievementAuto(x, y));
        }
        return res;
    }

    /**
     * Generate a list of goals. The distance between any two goals must be equal or greater than the minimum distance
    */
    public static List<Position> genGoals(int mapSize, int goalCount, Position except, Random rm, int minDistance) {
        List<Position> goals = new ArrayList<>();
        while (goals.size() < goalCount) {
            int x = rm.nextInt(mapSize);
            int y = rm.nextInt(mapSize);
            Position newGoal = new Position(x, y);

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

    private static boolean exceedMinDistance(Position newGoal, List<Position> goals, int minDistance) {
        for (Position goal : goals) {
            int curDistance = calculateDistance(newGoal, goal);
            if (curDistance < minDistance) {
                return true;
            }
        }
        return false;
    }

    public static HashMap<Position, Norm> genNorms(int mapSize, int numOfNorms, double avgPenalty, Position except, Random rm) {
        HashMap<Position, Norm> norms = new HashMap<>();
        while (norms.size() < numOfNorms) {
            int x = rm.nextInt(mapSize);
            int y = rm.nextInt(mapSize);
            Position position = new Position(x, y);
            if (position.equals(except)) {
                continue;
            }
            double penaltyValue = gaussian(avgPenalty, 0.01, 0.01, rm);
            norms.put(position, new Norm(position, penaltyValue));
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

    public static List<Position> cloneCells(List<Position> positions) {
        List<Position> cloneSet = new ArrayList<>();
        for (Position position : positions) {
            cloneSet.add(position);
        }
        return cloneSet;
    }
}
