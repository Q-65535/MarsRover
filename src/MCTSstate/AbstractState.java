package MCTSstate;

import agent.MoveAction;
import running.Utils;
import world.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractState implements Cloneable {
    public static Random rm = new Random(Utils.SEED);
    public abstract void exeAct(MoveAction act);

    public abstract void exeJump(Cell target);

    public abstract void exeJump(int x, int y);

    public abstract double evaluateState();

    @Override
    public abstract AbstractState clone();

    public abstract AbstractState randomSim();

    /**
     * Get all possible actions that can be executed in current state
     */
    public abstract ArrayList<MoveAction> getPossibleNextCs();
}
