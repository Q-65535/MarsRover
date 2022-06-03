package running;

import agent.*;
import graphic.EnvironmentDisplayer;
import world.Cell;
import world.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Set;
import static running.Utils.*;

import static running.Default.*;

public class ResultProducer {
    /**
     * The result directory
     */
    public static final String RESULT_DIR = "C:\\Users\\GB\\Documents\\projects\\res_results\\MS_results";
    public static final String doubleFormatter = "%.3f";
    static EnvironmentDisplayer displayer = new EnvironmentDisplayer();
    public static final int maxMultiplier = 9;
    public static final int maxGoalNum = 15;
    public static final int repetitionCount = 50;

    /**
     * experiment a type of agent and write the result to a file
     */
    public void expAgent(String agentType, String fileName) {
        double[][] consumptionRecords = new double[maxGoalNum + 1][maxMultiplier + 1];
        for (int goalNum = 1; goalNum <= maxGoalNum; goalNum++) {
            for (int multiplier = 2; multiplier <= maxMultiplier; multiplier++) {
                int capacity = def_map_size * multiplier;
                // always init the random object to make sure consistency
                Random random = new Random(SEED);
                for (int i = 0; i < repetitionCount; i++) {

                    Set<Cell> targets = randomGenerateTargetPositions(def_map_size, goalNum, def_recharge_position, random);
                    AbstractAgent agent = genNewAgent(agentType, capacity, targets);
                    Environment environment = new Environment(def_map_size, def_recharge_position, agent, def_act_consumption);

                    boolean running = true;
                    while (running) {
                        running = environment.run();
//                        displayer.display(environment);
                    }
                    // add consumption value to record
                    consumptionRecords[goalNum][multiplier] += agent.getTotalFuelConsumption();
                }
            }
        }
        // get average value
        for (int i = 0; i < consumptionRecords.length; i++) {
            for (int j = 0; j < consumptionRecords[i].length; j++) {
                consumptionRecords[i][j] /= 50;
            }
        }
        matrixToFile(consumptionRecords, join(RESULT_DIR, fileName));
    }

    private AbstractAgent genNewAgent(String agentType, int capacity, Set<Cell> goals) {
        AbstractAgent agent;
        switch (agentType) {
            case "mcts":
                agent = new MCTSAgent(def_initial_Position, goals, def_recharge_position, capacity, def_act_consumption);
                break;
            case "profifo":
                agent = new ProactiveFIFOAgent(def_initial_Position, goals, def_recharge_position, capacity, def_act_consumption);
                break;
            case "fifo":
                agent = new FIFOAgent(def_initial_Position, goals, def_recharge_position, capacity, def_act_consumption);
                break;
            case "greedy":
                agent = new GreedyAgent(def_initial_Position, goals, def_recharge_position, capacity, def_act_consumption);
                break;
            default:
                throw new RuntimeException("agent type name is not valid: " + agentType);
        }
        return agent;
    }

    /**
     * transform the result matrix to a text file
     */
    private void matrixToFile(double[][] matrix, File resultFile) {
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
