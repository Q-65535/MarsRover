package agent;

import running.Utils;
import world.Cell;

import java.sql.Array;
import java.util.*;

public abstract class AbstractAgent {
    public static Random rm = new Random(Utils.SEED);
    Cell currentPosition;
    MoveAction currentAct;
    Set<Cell> targetPositions;
    Cell currentTarget;
    List<Cell> achievedGoalCells;
    Cell rechargePosition;
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
    int actFuelConsumption;

    public AbstractAgent(Cell currentPosition, Set<Cell> targetPositions, Cell rechargePosition, int maxCapacity, int actFuelConsumption) {
        init();
        this.currentPosition = currentPosition;
        this.targetPositions = targetPositions;
        this.rechargePosition = rechargePosition;
        this.maxCapacity = maxCapacity;
        this.currentFuel = maxCapacity;
        this.actFuelConsumption = actFuelConsumption;
    }


    private void init() {
        targetPositions = new HashSet<>();
        achievedGoalCells = new ArrayList<>();
        totalFuelConsumption = 0;
        rechargeFuelConsumption = 0;
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

    public abstract boolean reasoning();

    public MoveAction execute() {
        return currentAct;
    }

    /**
     * Generate an action based on the target position and agent current position
     */
    MoveAction getActMoveTo(Cell target) {
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

        // randomly pick a possible action
        return acts.get(rm.nextInt(acts.size()));
    }

    /**
     * Calculate the distance between current position and target position
     * @param target the target position
     */
    int calculateDistance(Cell target) {
        return calculateDistance(currentPosition, target);
    }

    /**
     * Calculate the distance between any to cells
     */
    int calculateDistance(Cell from, Cell to) {
        int xDiff = Math.abs(from.getX() - to.getX());
        int yDiff = Math.abs(from.getY() - to.getY());
        return xDiff + yDiff;
    }

    /**
     * Estimate how much fuel will be consumed if travel to target position
     */
    int estimateFuelConsumption(Cell target) {
        int distance = calculateDistance(target);
        return distance * actFuelConsumption;
    }

    int estimateFuelConsumption(Cell from, Cell to) {
        int distance = calculateDistance(from, to);
        return distance * actFuelConsumption;
    }

    public void updatePosition(Cell newPosition) {
        this.currentPosition = newPosition;
    }

    public void updatePosition(int x, int y) {
        this.currentPosition = new Cell(x, y);
    }

    /**
     * different types of agent have different update strategy
     */
    public abstract void updateGoal();

    public void consumeFuel(int amount) {
        if (amount > this.currentFuel) {
            throw new RuntimeException("current fuel is not enough");
        }
        this.currentFuel -= amount;

        // update the total fuel records
        totalFuelConsumption +=amount;
        if (isGoToRecharge) {
            rechargeFuelConsumption += amount;
        }
    }

    /**
     * if current position is recharge position, recharge the fuel
     */
    public void updateFuel() {
        if (currentPosition.equals(rechargePosition)) {
            currentFuel = maxCapacity;
        }
    }
}

