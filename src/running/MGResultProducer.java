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
    public static final int repetitionCount = 50;
    public static final int def_num_goals = 10;
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

    public MGResultProducer(String RESULT_DIR, int maxMultiplier, int maxGoalNum, int maxPostGoalTimeGap) {
        this.RESULT_DIR = RESULT_DIR;
        this.maxMultiplier = maxMultiplier;
        this.maxGoalNum = maxGoalNum;
        this.maxPostGoalTimeGap = maxPostGoalTimeGap;
    }

    /**
     * experiment a type of agent and write the result to a file
     */
    public void expAgentVaryGoalCapacity(String agentType, String fileName) {
        double[][] consumptionRecords = new double[maxGoalNum + 1][maxMultiplier + 1];
        for (int goalNum = 1; goalNum <= maxGoalNum; goalNum++) {
            for (int multiplier = 2; multiplier <= maxMultiplier; multiplier++) {
                int capacity = def_map_size * multiplier;
                // always init the random object to make sure consistency
                Random random = new Random(SEED);
                for (int i = 0; i < repetitionCount; i++) {
                    List<Cell> targets = Default.genGoals(def_map_size, goalNum, def_recharge_position, random);
                    AbstractAgent agent = genNewAgent(agentType, capacity);
                    Environment environment = new Environment(def_map_size, def_recharge_position, agent, def_act_consumption, targets);

                    boolean running = true;
                    while (running) {
                        running = environment.run();
//                        displayer.display(environment);
                    }
                    // add consumption value to record
                    consumptionRecords[goalNum][multiplier] += agent.getTotalFuelConsumption();
                }
                System.out.println("goal: " + goalNum +", tank: " + multiplier * def_map_size + ", avg consumption: " + consumptionRecords[goalNum][multiplier] / repetitionCount);
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
     * experiment a type of agent and write the result to a file
     */
    public void expAgentVaryTimeGapCapacity(String agentType, String fileName) {
        double[][] consumptionRecords = new double[maxPostGoalTimeGap + 1][maxMultiplier + 1];
        for (int timeGap = 1; timeGap <= maxPostGoalTimeGap; timeGap++) {
            for (int multiplier = 2; multiplier <= maxMultiplier; multiplier++) {
                int capacity = def_map_size * multiplier;
                // always init the random object to make sure consistency
                Random random = new Random(SEED);
                for (int i = 0; i < repetitionCount; i++) {
                    List<Cell> targets = Default.genGoals(def_map_size, def_num_goals, def_recharge_position, random);
                    AbstractAgent agent = genNewAgent(agentType, capacity);
                    Environment environment = new Environment(def_map_size, def_recharge_position, agent, def_act_consumption, targets, timeGap);

                    boolean running = true;
                    while (running) {
                        running = environment.run();
//                        displayer.display(environment);
                    }
                    // add consumption value to record
                    consumptionRecords[timeGap][multiplier] += agent.getTotalFuelConsumption();
                }
                System.out.println("timeGap: " + timeGap +", tank: " + multiplier * def_map_size + ", avg consumption: " + consumptionRecords[timeGap][multiplier] / repetitionCount);
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
    AbstractAgent genNewAgent(String agentType, int capacity) {
        AbstractAgent agent;
        switch (agentType) {
            case "mcts":
                agent = new MCTSAgent(def_initial_Position, def_recharge_position, capacity, def_act_consumption);
                break;
            case "spmcts":
                agent = new SPMCTSAgent(def_initial_Position, def_recharge_position, capacity, def_act_consumption);
                break;
            case "profifo":
                agent = new ProactiveFIFOAgent(def_initial_Position, def_recharge_position, capacity, def_act_consumption);
                break;
            case "fifo":
                agent = new FIFOAgent(def_initial_Position, def_recharge_position, capacity, def_act_consumption);
                break;
            case "greedy":
                agent = new GreedyAgent(def_initial_Position, def_recharge_position, capacity, def_act_consumption);
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
}
