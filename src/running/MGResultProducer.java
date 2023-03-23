package running;

import agent.*;
import graphic.EnvironmentDisplayer;
import world.Boundary;
import world.Cell;
import world.Environment;
import world.Norm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static running.Utils.*;

import static running.Default.*;

public class MGResultProducer {
    public static final String doubleFormatter = "%.3f";
    public static final int infCapacity = Integer.MAX_VALUE;
    static EnvironmentDisplayer displayer = new EnvironmentDisplayer();
    /**
     * The result directory
     */
    public final String RESULT_DIR;

    private int repetitionCount = 100;
    private boolean isProduceFile = false;
    private boolean isDrawGraphic = false;

	private int goalCountStart = 1;
	private int goalCountEnd = 15;
	private int goalCountStep = 1;
	private int defGoalCount = 10;

    /**
     * Capacity = multiplier x capStep
     */
	private int capStartMultiplier = 2;
	private int capEndMultiplier = 10;
	private int capStep = 20;

	private double defPenalty = 0.1;

	private int normCountStartMultiplier = 0;
	private int normCountEndMultiplier = 5;
	private int normCountStep = 10;
	private int defNormCount = 0;

	private int intervalStart = 1;
	private int intervalEnd = 15;
	private int intervalStep = 1;
	private int defInterval = 0;


    public MGResultProducer(String RESULT_DIR) {
        this.RESULT_DIR = RESULT_DIR;
        // create the new dir
        new File(RESULT_DIR).mkdir();
    }

	public void setRepetitionCount(int count) {
		this.repetitionCount = count;
	}

    public void enableProduceFile() {
        this.isProduceFile = true;
    }

    public void enableDrawGraphic() {
        this.isDrawGraphic = true;
    }

	public void setGoalCount(int goalCount) {
		this.defGoalCount = goalCount;
	}

	public void setInterval(int interval) {
		this.defInterval = interval;
	}

	public void setGoalRange(int start, int end, int step) {
		this.goalCountStart = start;
		this.goalCountEnd = end;
		this.goalCountStep = step;
	}

	public void setCapRange(int startMultiplier, int endMultiplier, int step) {
		this.capStartMultiplier = startMultiplier;
		this.capEndMultiplier = endMultiplier;
		this.capStep = step;
	}

	public void setNormRange(int startMultiplier, int endMultiplier, int step) {
		this.normCountStartMultiplier = startMultiplier;
		this.normCountEndMultiplier = endMultiplier;
		this.normCountStep = step;
	}

	public void setIntervalRange(int start, int end, int step) {
		this.intervalStart = start;
		this.intervalEnd = end;
		this.intervalStep = step;
	}

    /**
     * experiment a type of agent with different number of goals and different capacity, then write the result to a file
     */
    public void exp_goalX_capY(String agentType) {
        double[][] consumptionRecords = new double[goalCountEnd + 1][capEndMultiplier + 1];
        long[][] timeRecords = new long[goalCountEnd + 1][capEndMultiplier + 1];
        for (int goalCount = goalCountStart; goalCount <= goalCountEnd; goalCount += goalCountStep) {
            for (int capMultiplier = capStartMultiplier; capMultiplier <= capEndMultiplier; capMultiplier++) {
                int capacity = capStep * capMultiplier;
                // always init the random object to make sure consistency
                Random random = new Random(SEED);
                for (int i = 0; i < repetitionCount; i++) {
                    List<Cell> goals = Default.genGoals(def_map_size, goalCount, def_recharge_position, random);
                    AbstractAgent agent = genNewAgent(agentType, capacity);
                    Environment environment = new Environment(agent, goals, defInterval);
                    // Record time
                    long begin = System.currentTimeMillis();
                    boolean running = true;
                    while (running) {
                        running = environment.run();
                        if (isDrawGraphic) {
                            displayer.display(environment);
                        }
                    }
                    long end = System.currentTimeMillis();
                    long timeCons = end - begin;
                    if (agent.getAchievedGoalCount() != goalCount) {
                        throw new RuntimeException("bug!! achieved goal:" + agent.getAchievedGoalCount() + " target: " + goalCount);
                    }
                    // add consumption value and time to record
                    consumptionRecords[goalCount][capMultiplier] += agent.getTotalFuelConsumption();
                    timeRecords[goalCount][capMultiplier] += (timeCons);
                }
                System.out.println("goal: " + goalCount +", battery: " + capacity + ", avg cons: " + consumptionRecords[goalCount][capMultiplier] / repetitionCount);
                System.out.println("time cons: " + timeRecords[goalCount][capMultiplier] / repetitionCount);
            }
        }
        // get average value
        for (int i = 0; i < consumptionRecords.length; i++) {
            for (int j = 0; j < consumptionRecords[i].length; j++) {
                consumptionRecords[i][j] /= repetitionCount;
                timeRecords[i][j] /= repetitionCount;
            }
        }
        if (isProduceFile) {
            matrixToFile(consumptionRecords, join(RESULT_DIR,  "cons_" + agentType + ".txt"));
            matrixToFile(timeRecords, join(RESULT_DIR, "time_" + agentType + ".txt"));
        }
    }

    /**
     * experiment a type of agent with differrent posting goal intervals and different capacity,
     * then write the result to a file
     */
	//@Incomplete: We need the number of norms to be specifed.
    public void exp_intervalX_capY(String agentType, int goalCount) {
        double[][] consumptionRecords = new double[intervalEnd + 1][capEndMultiplier + 1];
        for (int interval = 1; interval <= intervalEnd; interval++) {
            for (int capMultiplier = 2; capMultiplier <= capEndMultiplier; capMultiplier++) {
                int capacity = def_map_size * capMultiplier;
                // always init the random object to make sure consistency
                Random random = new Random(SEED);
                for (int i = 0; i < repetitionCount; i++) {
                    List<Cell> goals = Default.genGoals(def_map_size, goalCount, def_recharge_position, random);
                    AbstractAgent agent = genNewAgent(agentType, capacity);
                    Environment environment = new Environment(agent, goals, interval);

                    boolean running = true;
                    while (running) {
                        running = environment.run();
                        if (isDrawGraphic) {
                            displayer.display(environment);
                        }
                    }
                    // add consumption value to record
                    consumptionRecords[interval][capMultiplier] += agent.getTotalFuelConsumption();
                }
                System.out.println("interval: " + interval +", tank: " + capacity + ", avg consumption: " + consumptionRecords[interval][capMultiplier] / repetitionCount);
            }
        }
        // get average value
        for (int i = 0; i < consumptionRecords.length; i++) {
            for (int j = 0; j < consumptionRecords[i].length; j++) {
                consumptionRecords[i][j] /= repetitionCount;
            }
        }
        if (isProduceFile) {
            matrixToFile(consumptionRecords, join(RESULT_DIR, agentType + ".txt"));
        }
    }




	/**
	 * norm experiments. (the capacity is infinite)
	 */
    public void norm_exp_intervalX_goalY(String agentType) {
        double[][] penaltyRecords = new double[intervalEnd + 1][goalCountEnd + 1];
        double[][] consumptionRecords = new double[intervalEnd + 1][goalCountEnd + 1];
        double[][] aggregateRecords = new double[intervalEnd + 1][goalCountEnd + 1];


		for (int interval = 1; interval <= intervalEnd; interval++) {
			for (int goalCount = goalCountStart; goalCount <= goalCountEnd; goalCount += goalCountStep) {
				int capacity = infCapacity;
				Random random = new Random(SEED);
				for (int i = 0; i < repetitionCount; i++) {
                    List<Cell> goals = genGoals(def_map_size, goalCount, def_initial_Position, random);
                    AbstractAgent agent = genNewAgent(agentType, capacity);
                    Environment environment = new Environment(agent, goals, interval);
                    boolean running = true;
                    while (running) {
                        running = environment.run();
                        if (isDrawGraphic) {
                            displayer.display(environment);
                        }
                    }
                    // add consumption value to record
                    penaltyRecords[interval][goalCount] += agent.getTotalPenalty();
                    consumptionRecords[interval][goalCount] += agent.getTotalFuelConsumption();
                    aggregateRecords[interval][goalCount] += agent.getAggregateVal();
				}
                System.out.println("interval: " + interval);
                System.out.println("goalCount: " + goalCount);
                System.out.println("avg penalty: " + penaltyRecords[interval][goalCount] / repetitionCount);
                System.out.println("avg consumption: " + consumptionRecords[interval][goalCount] / repetitionCount);
                System.out.println("avg aggregate: " + aggregateRecords[interval][goalCount] / repetitionCount);
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
        System.out.println("------------------------------------------------------------Experiment: " + agentType + "---------------------------------------------------------------------------------------------");

        if (isProduceFile) {
            matrixToFile(penaltyRecords, join(RESULT_DIR, "penalty_" + agentType + ".txt"));
            matrixToFile(consumptionRecords, join(RESULT_DIR, "cons_" + agentType + ".txt"));
            matrixToFile(aggregateRecords, join(RESULT_DIR, "agg_" + agentType + ".txt"));
        }
    }










	/**
	 * state machine experiments.
	 */
    public void norm_exp_goalX_capY_varyInterval(String agentType, int interval) {
        double[][] penaltyRecords = new double[goalCountEnd + 1][capEndMultiplier + 1];
        double[][] consumptionRecords = new double[goalCountEnd + 1][capEndMultiplier + 1];
        double[][] aggregateRecords = new double[goalCountEnd + 1][capEndMultiplier + 1];
        int normCount = defNormCount;
        int normCountMultiplier = 0;

        for (int goalCount = goalCountStart; goalCount <= goalCountEnd; goalCount++) {
            for (int capMultiplier = capStartMultiplier; capMultiplier <= capEndMultiplier; capMultiplier++) {
                int capacity = capStep * capMultiplier;
                // always init the random object to make sure consistency
                Random goalRandomObj = new Random(SEED);
                Random boundaryRandomObj = new Random(SEED + 1);
                for (int i = 0; i < repetitionCount; i++) {
                    List<Cell> goals = genGoals(def_map_size, goalCount, def_initial_Position, goalRandomObj);
                    // @Incomplete: We need specify the boundary parameters outside.
                    // List<Boundary> boundaries = genBoundaries(def_map_size, 5, 5, boundaryRandomObj);
					// Just single middle long boundary.
                    List<Boundary> boundaries = genMiddleBoundary(def_map_size);
                    AbstractAgent agent = genNewAgent(agentType, capacity);
                    agent.setBoundaries(boundaries);
                    Environment environment = new Environment(agent, goals, interval);
                    boolean running = true;
                    while (running) {
                        running = environment.run();
                        if (isDrawGraphic) {
                            displayer.display(environment);
                        }
                    }
                    // add consumption value to record
                    penaltyRecords[goalCount][capMultiplier] += agent.getTotalPenalty();
                    consumptionRecords[goalCount][capMultiplier] += agent.getTotalFuelConsumption();
                    aggregateRecords[goalCount][capMultiplier] += agent.getAggregateVal();
                }
			}
                System.out.println("goalCount: " + goalCount + ", normCount: " + normCount);
                System.out.println("interval: " + interval);
                System.out.println("avg penalty: " + penaltyRecords[goalCount][normCountMultiplier] / repetitionCount);
                System.out.println("avg consumption: " + consumptionRecords[goalCount][normCountMultiplier] / repetitionCount);
                System.out.println("avg aggregate: " + aggregateRecords[goalCount][normCountMultiplier] / repetitionCount);
        }
        // get average value
        for (int i = 0; i < penaltyRecords.length; i++) {
            for (int j = 0; j < penaltyRecords[i].length; j++) {
                penaltyRecords[i][j] /= repetitionCount;
                consumptionRecords[i][j] /= repetitionCount;
                aggregateRecords[i][j] /= repetitionCount;
            }
        }
        System.out.println("------------------------------------------------------------Experiment: " + agentType + "---------------------------------------------------------------------------------------------");

        if (isProduceFile) {
            matrixToFile(penaltyRecords, join(RESULT_DIR, "penalty_" + agentType + ".txt"));
            matrixToFile(consumptionRecords, join(RESULT_DIR, "cons_" + agentType + ".txt"));
            matrixToFile(aggregateRecords, join(RESULT_DIR, "agg_" + agentType + ".txt"));
        }
    }



	/**
	 * state machine experiments.
	 */
    public void norm_exp_intervalX_capacityY_varyGoalCount(String agentType, int goalCount) {
        double[][] penaltyRecords = new double[intervalEnd + 1][capEndMultiplier + 1];
        double[][] consumptionRecords = new double[intervalEnd + 1][capEndMultiplier + 1];
        double[][] aggregateRecords = new double[intervalEnd + 1][capEndMultiplier + 1];


		for (int interval = 1; interval <= intervalEnd; interval++) {
			for (int capMultiplier = 2; capMultiplier <= capEndMultiplier; capMultiplier++) {
				int capacity = def_map_size * capMultiplier;
                Random goalRandomObj = new Random(SEED);
                Random boundaryRandomObj = new Random(SEED + 1);
				for (int i = 0; i < repetitionCount; i++) {
                    List<Cell> goals = genGoals(def_map_size, goalCount, def_initial_Position, goalRandomObj);
                    // @Incomplete: We need specify the boundary parameters outside.
                    // List<Boundary> boundaries = genBoundaries(def_map_size, 5, 5, boundaryRandomObj);
					// Just single middle long boundary.
                    List<Boundary> boundaries = genMiddleBoundary(def_map_size);
                    AbstractAgent agent = genNewAgent(agentType, capacity);
                    agent.setBoundaries(boundaries);
                    Environment environment = new Environment(agent, goals, interval);
                    boolean running = true;
                    while (running) {
                        running = environment.run();
                        if (isDrawGraphic) {
                            displayer.display(environment);
                        }
                    }
                    // add consumption value to record
                    penaltyRecords[interval][capMultiplier] += agent.getTotalPenalty();
                    consumptionRecords[interval][capMultiplier] += agent.getTotalFuelConsumption();
                    aggregateRecords[interval][capMultiplier] += agent.getAggregateVal();
				}
                System.out.println("goalCount: " + goalCount);
                System.out.println("interval: " + interval);
                System.out.println("avg penalty: " + penaltyRecords[goalCount][capMultiplier] / repetitionCount);
                System.out.println("avg consumption: " + consumptionRecords[goalCount][capMultiplier] / repetitionCount);
                System.out.println("avg aggregate: " + aggregateRecords[goalCount][capMultiplier] / repetitionCount);
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
        System.out.println("------------------------------------------------------------Experiment: " + agentType + "---------------------------------------------------------------------------------------------");

        if (isProduceFile) {
            matrixToFile(penaltyRecords, join(RESULT_DIR, "penalty_" + agentType + ".txt"));
            matrixToFile(consumptionRecords, join(RESULT_DIR, "cons_" + agentType + ".txt"));
            matrixToFile(aggregateRecords, join(RESULT_DIR, "agg_" + agentType + ".txt"));
        }
    }










	// Some utility functions.
    AbstractAgent genNewAgent(String agentType, int capacity) {
        AbstractAgent agent;
        switch (agentType) {
            case "PMCTS":
                agent = new MCTSAgent(capacity, true);
                break;
            case "RMCTS":
				// The default strategy is not proactive.
                agent = new MCTSAgent(capacity);
                break;
                // Infinite fuel mcts
            case "NMCTS":
                agent = new MCTSAgent(infCapacity);
                break;
            case "PMG":
                agent = new ProactiveFIFOAgent(capacity);
                break;
            case "RMG":
                agent = new FIFOAgent(capacity);
                break;
                // Infinite fuel fifo
            case "NMG":
                agent = new FIFOAgent(infCapacity);
                break;
            case "vBDI":
                agent = new VBDIAgent(capacity);
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
            resultFile.getParentFile().mkdirs(); // create dirs if not already exist
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
