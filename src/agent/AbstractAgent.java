package agent;

import running.Default;
import world.Calculator;
import world.Cell;
import world.Norm;
import static running.Default.*;

import java.util.*;

public abstract class AbstractAgent implements Cloneable {
    public static final int infinite_capacity = Integer.MAX_VALUE;
    Random rm = new Random(Default.SEED);

    /**
     * The total penalty received
     */
    double penalty;
    HashMap<Cell, Norm> norms;
    Cell currentPosition;
    MoveAction currentAct;
    List<Cell> goals;
    Cell currentGoal;
    List<Cell> achievedGoals;
    Cell rechargePosition;
    final int mapSize = def_map_size;
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

    public AbstractAgent(List<Cell> goals, int maxCapacity) {
        init();
        this.goals = new ArrayList<>(goals);
        this.maxCapacity = maxCapacity;
        this.currentFuel = maxCapacity;
    }

    public AbstractAgent(int maxCapacity) {
        init();
        this.maxCapacity = maxCapacity;
        this.currentFuel = maxCapacity;
    }

    private void init() {
        this.currentPosition = def_initial_Position;
        this.actFuelConsumption = def_act_consumption;
        this.rechargePosition = def_initial_Position;
        goals = new ArrayList<>();
        achievedGoals = new ArrayList<>();
        totalFuelConsumption = 0;
        rechargeFuelConsumption = 0;
        penalty = 0;
    }


    public void adoptGoal(Cell goal) {
        goals.add(goal);
    }

    public Cell getCurrentGoal() {
        return currentGoal;
    }

    public int getCurrentFuel() {
        return currentFuel;
    }

    public List<Cell> getGoals() {
        return goals;
    }

    public Cell getCurrentPosition() {
        return currentPosition;
    }

    public int getAchievedGoalCount() {
        return achievedGoals.size();
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
     * Generate an action based on the goal position and agent current position
     */
    public MoveAction getActMoveTo(Cell goal) {
        ArrayList<MoveAction> acts = getAllActMoveTo(goal);
        // randomly pick a possible action
        return acts.get(rm.nextInt(acts.size()));
    }

    public ArrayList<MoveAction> getAllActMoveTo(Cell goal) {

        ArrayList<MoveAction> acts = new ArrayList<>();
        int tx = goal.getX();
        int ty = goal.getY();

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
        //TODO properly deal with the case when the goal is just at the agent's position
        if (acts.size() == 0) {
            acts.add(MoveAction.RIGHT);
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

    public Cell getNearestGoal() {
        Cell nearestGoal = null;
        int minConsumption = Integer.MAX_VALUE;
        for (Cell goal : goals) {
            int consumption = this.estimateFuelConsumption(goal);
            if (consumption < minConsumption) {
                nearestGoal = goal;
                minConsumption = consumption;
            }
        }
        return nearestGoal;
    }

    /**
     * Calculate the distance between current position and goal position
     *
     * @param goal the goal position
     */
    int calculateDistance(Cell goal) {
        return Calculator.calculateDistance(currentPosition, goal);
    }

    /**
     * Estimate how much fuel will be consumed if travel to goal position
     */
    public int estimateFuelConsumption(Cell goal) {
        int distance = calculateDistance(goal);
        return distance * actFuelConsumption;
    }

    public int estimateFuelConsumption(Cell from, Cell to) {
        int distance = Calculator.calculateDistance(from, to);
        return distance * actFuelConsumption;
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

        if (norms.containskey(currentposition)) {
            norm relatednorm = norms.get(currentposition);
            this.penalty += relatednorm.getpenalty();
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

