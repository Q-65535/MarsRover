package agent;

import running.Default;
import world.Calculator;
import world.Cell;
import world.Norm;

import java.util.*;

public abstract class AbstractAgent implements Cloneable {
    public static final int def_act_fuel_consumption = 1;
    public static final int infinite_capacity = Integer.MAX_VALUE;
    Random rm = new Random(Default.SEED);

    /**
     * The total penalty received
     */
    double penalty;
    HashMap<Cell, Norm> norms;
    Cell currentPosition;
    MoveAction currentAct;
    Set<Cell> targetPositions;
    Cell currentTarget;
    List<Cell> achievedGoalCells;
    Cell rechargePosition;
    int mapSize;
    int maxCapacity;
    int currentFuel;
    int totalFuelConsumption;
    int rechargeFuelConsumption;
    /**
     * whether the current agent is going to recharge
     */
    boolean isGoToRecharge;
    /**
     * how much fuel will be consumed for each action (agent's perspective)
     */
    int actionFuelConsumption;

    public AbstractAgent(Cell currentPosition, Set<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actionFuelConsumption) {
        init();
        this.currentPosition = currentPosition;
        this.targetPositions = targetPositions;
        this.rechargePosition = rechargePosition;
        this.maxCapacity = maxCapacity;
        this.currentFuel = maxCapacity;
        this.actionFuelConsumption = actionFuelConsumption;
    }

    private void init() {
        targetPositions = new HashSet<>();
        achievedGoalCells = new ArrayList<>();
        totalFuelConsumption = 0;
        rechargeFuelConsumption = 0;
    }

    public void setGoals(Set<Cell> goals) {
        this.targetPositions = goals;
    }

    public void adoptGoal(Cell target) {
        targetPositions.add(target);
    }

    public Cell getCurrentTarget() {
        return currentTarget;
    }

    public int getCurrentFuel() {
        return currentFuel;
    }

    public Set<Cell> getTargetPositions() {
        return targetPositions;
    }

    public Cell getCurrentPosition() {
        return currentPosition;
    }

    public int getNumOfAchieved() {
        return achievedGoalCells.size();
    }

    public int getTotalFuelConsumption() {
        return totalFuelConsumption;
    }

    public int getRechargeFuelConsumption() {
        return rechargeFuelConsumption;
    }

    public abstract boolean reason();

    public MoveAction execute() {
        return currentAct;
    }

    /**
     * Generate an action based on the target position and agent current position
     */
    public MoveAction getActMoveTo(Cell target) {
        ArrayList<MoveAction> acts = getAllActMoveTo(target);
        // randomly pick a possible action
        return acts.get(rm.nextInt(acts.size()));
    }

    public ArrayList<MoveAction> getAllActMoveTo(Cell target) {

        ArrayList<MoveAction> acts = new ArrayList<>();
        int tx = target.getX();
        int ty = target.getY();

        if (tx > currentPosition.getX()) {
            acts.add(MoveAction.RIGHT);
        }
        if (tx < currentPosition.getX()) {
            acts.add(MoveAction.LEFT);
        }
        if (ty > currentPosition.getY()) {
            acts.add(MoveAction.UP);
        }
        if (ty < currentPosition.getY()) {
            acts.add(MoveAction.DOWN);
        }
        return acts;

    }

    /**
     * Get the next position if the given action is executed
     */
    public Cell getNextPosition(MoveAction act) {
        Cell currentPosition = this.getCurrentPosition();
        int curX = currentPosition.getX();
        int curY = currentPosition.getY();

        switch (act) {
            case UP -> {return new Cell(curX, curY + 1);}
            case DOWN -> {return new Cell(curX, curY - 1);}
            case LEFT -> {return new Cell(curX - 1, curY);}
            case RIGHT -> {return new Cell(curX + 1, curY);}
        }
        // error case
        return null;
    }

    /**
     * Calculate the distance between current position and target position
     *
     * @param target the target position
     */
    int calculateDistance(Cell target) {
        return Calculator.calculateDistance(currentPosition, target);
    }

    /**
     * Estimate how much fuel will be consumed if travel to target position
     */
    public int estimateFuelConsumption(Cell target) {
        int distance = calculateDistance(target);
        return distance * actionFuelConsumption;
    }

    public int estimateFuelConsumption(Cell from, Cell to) {
        int distance = Calculator.calculateDistance(from, to);
        return distance * actionFuelConsumption;
    }

    public void updatePosition(Cell newPosition) {
        this.currentPosition = newPosition;
    }

    public void updatePosition(int x, int y) {
        this.currentPosition = new Cell(x, y);
    }

    /**
     * different types of agent have different update strategies
     */
    public abstract void updateGoal();

    public void consumeFuel(int amount) {
        if (amount > this.currentFuel) {
            throw new RuntimeException("current fuel is not enough");
        }
        this.currentFuel -= amount;

        // update the total fuel records
        totalFuelConsumption += amount;
        if (isGoToRecharge) {
            rechargeFuelConsumption += amount;
        }
    }

    /**
     * if current position is recharge position, recharge the fuel
     */
    public void updateRecharge() {
        if (currentPosition.equals(rechargePosition)) {
            currentFuel = maxCapacity;
        }
    }

    /**
     * Estimate whether the agent needs to do recharge operation
     */
    boolean needRecharge() {
        return currentFuel <= estimateFuelConsumption(rechargePosition);
    }

    /**
     * update total penalty value according to current position and norms
     */
    public void updatePunish() {
        // if no norm, nothing happens
        if (norms == null) {
            return;
        }

        if (norms.containsKey(currentPosition)) {
            Norm relatedNorm = norms.get(currentPosition);
            this.penalty += relatedNorm.getPenalty();
        }
    }

    public double getTotalPenalty() {
        return penalty;
    }

    public Set<Cell> getNormPositions() {
        if (norms == null) {
            return new HashSet<>();
        }
        return norms.keySet();
    }
}

