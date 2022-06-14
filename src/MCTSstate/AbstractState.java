package MCTSstate;

import agent.MoveAction;
import running.Default;
import world.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractState implements Cloneable {
    Random rm = new Random();
    public abstract void exeAct(MoveAction act);

    public abstract double evaluateState();

    @Override
    public abstract AbstractState clone();

    public abstract AbstractState randomSim(List<MoveAction> actContainer);

    /**
     * Get all possible actions that can be executed in current state
     */
    public abstract ArrayList<MoveAction> getPossibleNextCs();
}
