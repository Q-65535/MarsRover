package running;

import agent.*;
import world.Cell;
import world.Environment;
import world.Norm;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import static running.Default.*;
import static running.Default.def_act_consumption;
import static running.Utils.join;

public class NormResultProducer extends MGResultProducer {
    public static final String penaltyResultPrefix = "penalty-";
    public static final String consumptionResultPrefix = "consumption-";
    public static final int def_penalty = 3;
    public static final int def_num_norms = 30;
    final int maxNormPenalty;
    final int maxNumOfNorms;

    public NormResultProducer(String RESULT_DIR, int maxGoalNum, int maxNormPenalty, int maxNumOfNorms) {
        super(RESULT_DIR, 0, maxGoalNum);
        this.maxNormPenalty = maxNormPenalty;
        this.maxNumOfNorms = maxNumOfNorms;
    }
    /**
     * experiment varying number of norms
     */
    public void expAgentVaryNumOfNorms(String agentType, String fileName) {
        double[][] penaltyRecords = new double[maxGoalNum + 1][maxNumOfNorms + 1];
        double[][] consumptionRecords = new double[maxGoalNum + 1][maxNumOfNorms + 1];

        for (int goalNum = 1; goalNum <= maxGoalNum; goalNum++) {
            for (int numOfNorm = 0; numOfNorm <= maxNumOfNorms; numOfNorm++) {
                // always init the random object to make sure consistency
                Random goalRandomObj = new Random(SEED);
                Random normRandomObj = new Random(SEED);
                for (int i = 0; i < repetitionCount; i++) {
                    HashMap<Cell, Norm> norms = randomGenerateNorms(def_map_size, numOfNorm, def_penalty, def_recharge_position, normRandomObj);
                    Set<Cell> targets = randomGenerateTargetPositions(def_map_size, goalNum, def_initial_Position, goalRandomObj);
                    AbstractAgent agent = genNewAgent(agentType, targets, norms);
                    Environment environment = new Environment(def_map_size, def_recharge_position, agent, def_act_consumption);

                    boolean running = true;
                    while (running) {
                        running = environment.run();
//                        displayer.display(environment);
                    }
                    // add consumption value to record
                    penaltyRecords[goalNum][numOfNorm] += agent.getTotalPenalty();
                    consumptionRecords[goalNum][numOfNorm] += agent.getTotalFuelConsumption();
                }
                System.out.println("penalty: " + penaltyRecords[goalNum][numOfNorm]);
                System.out.println("consumption: " + consumptionRecords[goalNum][numOfNorm]);
            }
        }
        // get average value
        for (int i = 0; i < penaltyRecords.length; i++) {
            for (int j = 0; j < penaltyRecords[i].length; j++) {
                penaltyRecords[i][j] /= repetitionCount;
                consumptionRecords[i][j] /= repetitionCount;
            }
        }
        matrixToFile(penaltyRecords, join(RESULT_DIR, penaltyResultPrefix + fileName));
        matrixToFile(consumptionRecords, join(RESULT_DIR, consumptionResultPrefix + fileName));
    }

    /**
     * Experiment varying norm penalty value
     */
    public void expAgentVaryNormPenalty(String agentType, String fileName) {
        double[][] penaltyRecords = new double[maxGoalNum + 1][maxNormPenalty + 1];
        double[][] consumptionRecords = new double[maxGoalNum + 1][maxNormPenalty + 1];
        for (int goalNum = 1; goalNum <= maxGoalNum; goalNum++) {
            for (int normPenalty = 0; normPenalty <= maxNormPenalty; normPenalty++) {
                // always init the random object to make sure consistency
                Random goalRandomObj = new Random(SEED);
                Random normRandomObj = new Random(SEED);
                for (int i = 0; i < repetitionCount; i++) {
                    HashMap<Cell, Norm> norms = randomGenerateNorms(def_map_size, def_num_norms, normPenalty, def_recharge_position, normRandomObj);
                    Set<Cell> targets = randomGenerateTargetPositions(def_map_size, goalNum, def_initial_Position, goalRandomObj);
                    AbstractAgent agent = genNewAgent(agentType, targets, norms);
                    Environment environment = new Environment(def_map_size, def_recharge_position, agent, def_act_consumption);

                    boolean running = true;
                    while (running) {
                        running = environment.run();
//                        displayer.display(environment);
                    }
                    // add consumption value to record
                    penaltyRecords[goalNum][normPenalty] += agent.getTotalPenalty();
                    consumptionRecords[goalNum][normPenalty] += agent.getTotalFuelConsumption();
                }
                System.out.println("penalty: " + penaltyRecords[goalNum][normPenalty]);
                System.out.println("consumption: " + consumptionRecords[goalNum][normPenalty]);
            }
        }
        // get average value
        for (int i = 0; i < penaltyRecords.length; i++) {
            for (int j = 0; j < penaltyRecords[i].length; j++) {
                penaltyRecords[i][j] /= repetitionCount;
                consumptionRecords[i][j] /= repetitionCount;
            }
        }
        matrixToFile(penaltyRecords, join(RESULT_DIR, penaltyResultPrefix + fileName));
        matrixToFile(consumptionRecords, join(RESULT_DIR, consumptionResultPrefix + fileName));
    }


    AbstractAgent genNewAgent(String agentType, Set<Cell> goals, HashMap<Cell, Norm> norms) {
        AbstractAgent agent;
        switch (agentType) {
            case "mcts":
                agent = new NMCTSAgent(def_initial_Position, goals, norms);
                break;
            case "spmcts":
                agent = new NSPMCTSAgent(def_initial_Position, goals, norms);
                break;
            case "vbdi":
                agent = new VBDIAgent(def_initial_Position, goals, norms);
                break;
            case "fifo":
                agent = new NFIFOAgent(def_initial_Position, goals, norms);
                break;
            default:
                throw new RuntimeException("agent type name is not valid: " + agentType);
        }
        return agent;
    }

}
