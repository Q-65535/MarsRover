package running;

import agent.*;
import world.Cell;
import world.Environment;
import world.Norm;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static running.Default.*;
import static running.Utils.join;

public class NormResultProducer extends MGResultProducer {
    public static final String penaltyResultPrefix = "penalty-";
    public static final String consumptionResultPrefix = "consumption-";
    public static final String aggregateResultPrefix = "aggregate-";
    public static final double def_penalty = 0.1;
    public static final int def_num_norms = 30;
    final int maxNormPenalty;
    final int maxNormCountMultiplier;

    public NormResultProducer(String RESULT_DIR, int maxGoalNum, int maxNormPenalty, int maxNormCountMultiplier, int defPostGoalTimeGap) {
        super(RESULT_DIR, 0, maxGoalNum, 0, defPostGoalTimeGap);
        this.maxNormPenalty = maxNormPenalty;
        this.maxNormCountMultiplier = maxNormCountMultiplier;
    }

    public NormResultProducer(String RESULT_DIR, int maxGoalNum, int maxNormPenalty, int maxNormCountMultiplier, int maxPostGoalTimeGap, int defPostGoalTimeGap) {
        super(RESULT_DIR, 0, maxGoalNum, maxPostGoalTimeGap, defPostGoalTimeGap);
        this.maxNormPenalty = maxNormPenalty;
        this.maxNormCountMultiplier = maxNormCountMultiplier;
    }
    /**
     * experiment varying number of norms
     */
    public void expAgentVaryNumOfNorms(String agentType, String fileName) {
        double[][] penaltyRecords = new double[maxGoalNum + 1][maxNormCountMultiplier + 1];
        double[][] consumptionRecords = new double[maxGoalNum + 1][maxNormCountMultiplier + 1];
        double[][] aggregateRecords = new double[maxGoalNum + 1][maxNormCountMultiplier + 1];

        for (int goalNum = 1; goalNum <= maxGoalNum; goalNum++) {
            // Changed gap jump to 10
            for (int normCountMultiplier = 0; normCountMultiplier <= maxNormCountMultiplier; normCountMultiplier++) {
                // The actual number of norms is multiplier times 10.
                int normNum = normCountMultiplier * 10;
                // always init the random object to make sure consistency
                Random goalRandomObj = new Random(SEED);
                // important: make sure that the random seeds for generating goals and goals are different
                Random normRandomObj = new Random(SEED + 1);
                for (int i = 0; i < repetitionCount; i++) {
                    HashMap<Cell, Norm> norms = genNorms(def_map_size, normNum, def_penalty, def_recharge_position, normRandomObj);
                    List<Cell> goals = Default.genGoals(def_map_size, goalNum, def_initial_Position, goalRandomObj);
                    AbstractAgent agent = genNewAgent(agentType, norms);
                    Environment environment = new Environment(agent, goals, defPostGoalTimeGap);

                    boolean running = true;
                    while (running) {
                        running = environment.run();
//                        displayer.display(environment);
                    }
                    // add consumption value to record
                    penaltyRecords[goalNum][normCountMultiplier] += agent.getTotalPenalty();
                    consumptionRecords[goalNum][normCountMultiplier] += agent.getTotalFuelConsumption();
                    aggregateRecords[goalNum][normCountMultiplier] += agent.getAggregateVal();
                }
                System.out.println("goalNum: " + goalNum + ", normNum: " + normNum);
                System.out.println("avg penalty: " + penaltyRecords[goalNum][normCountMultiplier] / repetitionCount);
                System.out.println("avg consumption: " + consumptionRecords[goalNum][normCountMultiplier] / repetitionCount);
                System.out.println("avg aggregate: " + aggregateRecords[goalNum][normCountMultiplier] / repetitionCount);
            }
        }
        // get average value
        for (int i = 0; i < penaltyRecords.length; i++) {
            for (int j = 0; j < penaltyRecords[i].length; j++) {
                penaltyRecords[i][j] /= repetitionCount;
                consumptionRecords[i][j] /= repetitionCount;
                aggregateRecords[i][j] /= repetitionCount;
            }
        }
        System.out.println("------------------------------------------------------------Experiment: " + fileName + "---------------------------------------------------------------------------------------------");
        matrixToFile(penaltyRecords, join(RESULT_DIR, penaltyResultPrefix + fileName));
        matrixToFile(consumptionRecords, join(RESULT_DIR, consumptionResultPrefix + fileName));
        matrixToFile(aggregateRecords, join(RESULT_DIR, aggregateResultPrefix + fileName));
    }

    /**
     * experiment a type of agent with differrent posting goal intervals and different capacity,
     * then write the result to a file
     */
    public void expAgentVaryTimeGapCapacity(String agentType, String fileName) {
        double[][] penaltyRecords = new double[maxPostGoalTimeGap + 1][maxNormCountMultiplier + 1];
        double[][] consumptionRecords = new double[maxPostGoalTimeGap + 1][maxNormCountMultiplier + 1];
        double[][] aggregateRecords = new double[maxGoalNum + 1][maxNormCountMultiplier + 1];
        for (int timeGap = 1; timeGap <= maxPostGoalTimeGap; timeGap++) {
            for (int normCountMultiplier = 0; normCountMultiplier <= maxNormCountMultiplier; normCountMultiplier++) {
                int normNum = normCountMultiplier * 10;
                // always init the random object to make sure consistency
                Random goalRandomObj = new Random(SEED);
                // important: make sure that the random seeds for generating goals and goals are different
                Random normRandomObj = new Random(SEED + 1);
                for (int i = 0; i < repetitionCount; i++) {
                    HashMap<Cell, Norm> norms = genNorms(def_map_size, normNum, def_penalty, def_recharge_position, normRandomObj);
                    List<Cell> goals = Default.genGoals(def_map_size, def_goal_count, def_initial_Position, goalRandomObj);
                    AbstractAgent agent = genNewAgent(agentType, norms);
                    Environment environment = new Environment(agent, goals, timeGap);

                    boolean running = true;
                    while (running) {
                        running = environment.run();
//                        displayer.display(environment);
                    }
                    // add consumption value to record
                    consumptionRecords[timeGap][normCountMultiplier] += agent.getTotalFuelConsumption();
                    penaltyRecords[timeGap][normCountMultiplier] += agent.getTotalPenalty();
                    aggregateRecords[timeGap][normCountMultiplier] += agent.getAggregateVal();
                }
                System.out.println("timeGap: " + timeGap +", norm number: " + normNum + ", avg consumption: " + consumptionRecords[timeGap][normCountMultiplier] / repetitionCount);
                System.out.println("avg aggregate: " + aggregateRecords[timeGap][normCountMultiplier] / repetitionCount);
            }
        }
        // get average value
        for (int i = 0; i < consumptionRecords.length; i++) {
            for (int j = 0; j < consumptionRecords[i].length; j++) {
                consumptionRecords[i][j] /= repetitionCount;
                penaltyRecords[i][j] /= repetitionCount;
                aggregateRecords[i][j] /= repetitionCount;
            }
        }
        matrixToFile(consumptionRecords, join(RESULT_DIR, consumptionResultPrefix + fileName));
        matrixToFile(penaltyRecords, join(RESULT_DIR, penaltyResultPrefix + fileName));
        matrixToFile(aggregateRecords, join(RESULT_DIR, aggregateResultPrefix + fileName));
    }

    /**
     * Experiment varying norm penalty value
     */
//    public void expAgentVaryNormPenalty(String agentType, String fileName) {
//        double[][] penaltyRecords = new double[maxGoalNum + 1][maxNormPenalty + 1];
//        double[][] consumptionRecords = new double[maxGoalNum + 1][maxNormPenalty + 1];
//        for (int goalNum = 1; goalNum <= maxGoalNum; goalNum++) {
//            for (int normPenalty = 0; normPenalty <= maxNormPenalty; normPenalty++) {
//                // always init the random object to make sure consistency
//                Random goalRandomObj = new Random(SEED);
//                Random normRandomObj = new Random(SEED);
//                for (int i = 0; i < repetitionCount; i++) {
//                    HashMap<Cell, Norm> norms = genNorms(def_map_size, def_num_norms, normPenalty, def_recharge_position, normRandomObj);
//                    List<Cell> goals = Default.genGoals(def_map_size, goalNum, def_initial_Position, goalRandomObj);
//                    AbstractAgent agent = genNewAgent(agentType, norms);
//                    Environment environment = new Environment(agent);
//
//                    boolean running = true;
//                    while (running) {
//                        running = environment.run();
////                        displayer.display(environment);
//                    }
//                    // add consumption value to record
//                    penaltyRecords[goalNum][normPenalty] += agent.getTotalPenalty();
//                    consumptionRecords[goalNum][normPenalty] += agent.getTotalFuelConsumption();
//                }
//                System.out.println("penalty: " + penaltyRecords[goalNum][normPenalty]);
//                System.out.println("consumption: " + consumptionRecords[goalNum][normPenalty]);
//            }
//        }
//        // get average value
//        for (int i = 0; i < penaltyRecords.length; i++) {
//            for (int j = 0; j < penaltyRecords[i].length; j++) {
//                penaltyRecords[i][j] /= repetitionCount;
//                consumptionRecords[i][j] /= repetitionCount;
//            }
//        }
//        matrixToFile(penaltyRecords, join(RESULT_DIR, penaltyResultPrefix + fileName));
//        matrixToFile(consumptionRecords, join(RESULT_DIR, consumptionResultPrefix + fileName));
//    }


    AbstractAgent genNewAgent(String agentType, HashMap<Cell, Norm> norms) {
        AbstractAgent agent;
        switch (agentType) {
            case "mcts":
                agent = new NMCTSAgent(norms);
                break;
            case "spmcts":
                agent = new NSPMCTSAgent(norms);
                break;
            case "vbdi":
                agent = new VBDIAgent(norms);
                break;
            case "fifo":
                agent = new NFIFOAgent(norms);
                break;
            default:
                throw new RuntimeException("agent type name is not valid: " + agentType);
        }
        return agent;
    }

}
