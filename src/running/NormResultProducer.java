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

public class NormResultProducer extends ResultProducer {
    final int maxNormPenalty;
    final int maxNumOfNorms;

    public NormResultProducer(String RESULT_DIR, int maxGoalNum, int maxNormPenalty, int maxNumOfNorms) {
        super(RESULT_DIR, 0, maxGoalNum);
        this.maxNormPenalty = maxNormPenalty;
        this.maxNumOfNorms = maxNumOfNorms;
    }
    /**
     * experiment a type of agent and write the result to a file
     */
    public void expAgentVaryNumOfNorms(String agentType, String fileName) {
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
                        displayer.display(environment);
                    }
                    // add consumption value to record
                    consumptionRecords[goalNum][numOfNorm] += agent.getTotalPenalty();
                }
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

    public void expAgentVaryNormPenalty(String agentType, String fileName) {
        double[][] consumptionRecords = new double[maxGoalNum + 1][maxNumOfNorms + 1];
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
                    consumptionRecords[goalNum][normPenalty] += agent.getTotalFuelConsumption();
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


    AbstractAgent genNewAgent(String agentType, Set<Cell> goals, HashMap<Cell, Norm> norms) {
        AbstractAgent agent;
        switch (agentType) {
            case "mcts":
                agent = new NMCTSAgent(def_initial_Position, goals, norms);
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
