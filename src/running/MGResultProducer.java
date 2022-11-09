package running;

import agent.*;
import graphic.EnvironmentDisplayer;
import world.Cell;
import world.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static running.Utils.*;

import static running.Default.*;

public class MGResultProducer {
    public static final String doubleFormatter = "%.3f";
    public static final int repetitionCount = 100;
    public static final int def_goal_count = 10;
    public static final int infCapacityAmount = 99999999;
    static EnvironmentDisplayer displayer = new EnvironmentDisplayer();
    /**
     * The result directory
     */
    public final String RESULT_DIR;
    /**
     * Capacity is multiplier x mapSize
     */
    public final int maxMultiplier;
    public final int maxGoalNum;
    public final int maxPostGoalTimeGap;
    public final int defPostGoalTimeGap;
    // TODO refactor: properly set the parameters
    public final int maxDispersionDegree = 8;

    public MGResultProducer(String RESULT_DIR, int maxMultiplier, int maxGoalNum, int maxPostGoalTimeGap, int defPostGoalTimeGap) {
        this.RESULT_DIR = RESULT_DIR;
        this.maxMultiplier = maxMultiplier;
        this.maxGoalNum = maxGoalNum;
        this.maxPostGoalTimeGap = maxPostGoalTimeGap;
        this.defPostGoalTimeGap = defPostGoalTimeGap;
        // create the new dir
        new File(RESULT_DIR).mkdir();
    }

    /**
     * experiment a type of agent with different number of goals and different capacity, then write the result to a file
     */
    public void expAgentVaryGoalCapacity(String agentType, String fileName) {
        double[][] consumptionRecords = new double[maxGoalNum + 1][maxMultiplier + 1];
        long[][] timeRecords = new long[maxGoalNum + 1][maxMultiplier + 1];
        for (int goalNum = 1; goalNum <= maxGoalNum; goalNum++) {
            for (int multiplier = 2; multiplier <= maxMultiplier; multiplier++) {
                int capacity = def_map_size * multiplier;
                // always init the random object to make sure consistency
                Random random = new Random(SEED);
                for (int i = 0; i < repetitionCount; i++) {
                    List<Cell> goals = Default.genGoals(def_map_size, goalNum, def_recharge_position, random);
                    AbstractAgent agent = genNewAgent(agentType, capacity);
                    Environment environment = new Environment(agent, goals, defPostGoalTimeGap);
                    // Record time
                    long begin = System.currentTimeMillis();
                    boolean running = true;
                    while (running) {
                        running = environment.run();
//                        displayer.display(environment);
                    }
                    long end = System.currentTimeMillis();
                    long timeTaken = end - begin;
                    // add consumption value and time to record
                    consumptionRecords[goalNum][multiplier] += agent.getTotalFuelConsumption();
                    timeRecords[goalNum][multiplier] += (timeTaken);
                }
                System.out.println("goal: " + goalNum +", tank: " + capacity + ", avg consumption: " + consumptionRecords[goalNum][multiplier] / repetitionCount);
                System.out.println("time taken: " + timeRecords[goalNum][multiplier] / repetitionCount);
            }
        }
        // get average value
        for (int i = 0; i < consumptionRecords.length; i++) {
            for (int j = 0; j < consumptionRecords[i].length; j++) {
                consumptionRecords[i][j] /= repetitionCount;
                timeRecords[i][j] /= repetitionCount;
            }
        }
        matrixToFile(consumptionRecords, join(RESULT_DIR,  fileName));
//        matrixToFile(timeRecords, join(RESULT_DIR, "time_" + fileName));
    }

    /**
     * experiment a type of agent with differrent posting goal intervals and different capacity,
     * then write the result to a file
     */
    public void expAgentVaryTimeGapCapacity(String agentType, String fileName) {
        double[][] consumptionRecords = new double[maxPostGoalTimeGap + 1][maxMultiplier + 1];
        for (int timeGap = 1; timeGap <= maxPostGoalTimeGap; timeGap++) {
            for (int multiplier = 2; multiplier <= maxMultiplier; multiplier++) {
                int capacity = def_map_size * multiplier;
                // always init the random object to make sure consistency
                Random random = new Random(SEED);
                for (int i = 0; i < repetitionCount; i++) {
                    List<Cell> goals = Default.genGoals(def_map_size, def_goal_count, def_recharge_position, random);
                    AbstractAgent agent = genNewAgent(agentType, capacity);
                    Environment environment = new Environment(agent, goals, timeGap);

                    boolean running = true;
                    while (running) {
                        running = environment.run();
//                        displayer.display(environment);
                    }
                    // add consumption value to record
                    consumptionRecords[timeGap][multiplier] += agent.getTotalFuelConsumption();
                }
                System.out.println("timeGap: " + timeGap +", tank: " + capacity + ", avg consumption: " + consumptionRecords[timeGap][multiplier] / repetitionCount);
            }
        }
        // get average value
        for (int i = 0; i < consumptionRecords.length; i++) {
            for (int j = 0; j < consumptionRecords[i].length; j++) {
                consumptionRecords[i][j] /= repetitionCount;
            }
        }
        matrixToFile(consumptionRecords, join(RESULT_DIR, fileName));
    }

    /**
     * experiment a type of agent with different dispersion degrees and different capacity, then write the result to a file
     */
    public void expAgentVaryDispersionCapacity(String agentType, String fileName) {
        double[][] consumptionRecords = new double[maxDispersionDegree + 1][maxMultiplier + 1];
        for (int degree = 0; degree <= maxDispersionDegree; degree++) {
            for (int multiplier = 2; multiplier <= maxMultiplier; multiplier++) {
                int capacity = def_map_size * multiplier;
                // always init the random object to make sure consistency
                Random random = new Random(SEED);
                for (int i = 0; i < repetitionCount; i++) {
                    List<Cell> goals = Default.genGoals(def_map_size, def_goal_count, def_recharge_position, random, degree);
                    AbstractAgent agent = genNewAgent(agentType, capacity);
                    Environment environment = new Environment(agent, goals);

                    boolean running = true;
                    while (running) {
                        running = environment.run();
                        displayer.display(environment);
                    }
                    // add consumption value to record
                    consumptionRecords[degree][multiplier] += agent.getTotalFuelConsumption();
                }
                System.out.println("dispersion degree: " + degree +", capacity: " + multiplier * def_map_size + ", avg consumption: " + consumptionRecords[degree][multiplier] / repetitionCount);
            }
        }
        // get average value
        for (int i = 0; i < consumptionRecords.length; i++) {
            for (int j = 0; j < consumptionRecords[i].length; j++) {
                consumptionRecords[i][j] /= repetitionCount;
            }
        }
//        matrixToFile(consumptionRecords, join(RESULT_DIR, fileName));
    }

    AbstractAgent genNewAgent(String agentType, int capacity) {
        AbstractAgent agent;
        switch (agentType) {
            case "promcts":
                agent = new MCTSAgent(capacity, true);
                break;
            case "reamcts":
                agent = new MCTSAgent(capacity, false);
                break;
                // Infinite fuel mcts
            case "infmcts":
                agent = new MCTSAgent(infCapacityAmount, false);
                break;
//            case "spmcts":
//                agent = new SPMCTSAgent(capacity);
//                break;
            case "profifo":
                agent = new ProactiveFIFOAgent(capacity);
                break;
            case "fifo":
                agent = new FIFOAgent(capacity);
                break;
                // Infinite fuel fifo
            case "inffifo":
                agent = new FIFOAgent(infCapacityAmount);
                break;
            case "greedy":
                agent = new GreedyAgent(capacity);
                break;
            default:
                throw new RuntimeException("agent type name is not valid: " + agentType);
        }
        return agent;
    }

    /**
     * transform the result matrix to a text file
     */
    void matrixToFile(double[][] matrix, File resultFile) {
        try {
            FileWriter writer = new FileWriter(resultFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    bufferedWriter.write(String.format(doubleFormatter, matrix[i][j]) + " ");
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * transform the result matrix to a text file
     */
    void matrixToFile(long[][] matrix, File resultFile) {
        try {
            FileWriter writer = new FileWriter(resultFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    bufferedWriter.write(matrix[i][j] + " ");
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
