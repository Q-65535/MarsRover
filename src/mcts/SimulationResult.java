package mcts;

public class SimulationResult {
    public static final double delta = 1e-6;
    final int achievedGoalCount;
    final int fuelConsumption;
    final double penaltyVal;
    final double aggregateVal;

    public SimulationResult(int achievedGoalCount, int fuelConsumption, double penaltyVal) {
        this.achievedGoalCount = achievedGoalCount;
        this.fuelConsumption = fuelConsumption;
        this.penaltyVal = penaltyVal;
        this.aggregateVal = (achievedGoalCount / (fuelConsumption + delta)) - penaltyVal;
    }

    double getAggregateVal() {
        return this.aggregateVal;
    }
}
