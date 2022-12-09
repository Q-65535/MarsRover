package MCTSstate;

import agent.Choice;
import agent.MoveAction;

import java.util.*;

public abstract class AbstractState implements Cloneable {
    public final static double delta = 1e-6;
    Random rm = new Random();
    public abstract void update(List<Choice> choices);

    public abstract double evaluateState();


    public abstract AbstractState randomSim();

    public abstract List<Choice> getChoicePath();

    /**
     * Get all possible actions that can be executed in current state
     */
    public abstract List<List<Choice>> getPossibleNextCs();

    public abstract int getAchievedGoalCount();

    @Override
    public abstract AbstractState clone();
}
