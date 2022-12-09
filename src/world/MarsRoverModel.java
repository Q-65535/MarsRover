package world;

import gpt.*;

import java.util.*;

// @Smell: Should we implement a Model interface (or abstract class)?
public class MarsRoverModel implements Cloneable {
    private Position curAgentPosition;
    private Position preAgentPosition;
    private int agentFuel;

    public MarsRoverModel() {}

    public MarsRoverModel(Position currentPosition, int currentFuel) {
        this.curAgentPosition = currentPosition;
        this.preAgentPosition = currentPosition;
        this.agentFuel = currentFuel;
    }

    public Position getPreAgentPosition() {
        return preAgentPosition;
    }

    public Position getCurAgentPosition() {
        return curAgentPosition;
    }

    public int getAgentFuel() {
        return this.agentFuel;
    }

    // Update this model according to the given model
    public void sync(MarsRoverModel marsRoverModel) {
        this.curAgentPosition = marsRoverModel.curAgentPosition;
        this.preAgentPosition = marsRoverModel.preAgentPosition;
        this.agentFuel = marsRoverModel.agentFuel;
    }

    public void setCurAgentPosition(Position p) {
        this.preAgentPosition = curAgentPosition;
        this.curAgentPosition = p;
    }

    public void setAgentFuel(int f) {
        this.agentFuel = f;
    }


    public void apply(AppliableFormula formula) {
        formula.apply(this);
    }

    private void apply(Literal l) {
        if (l.getState() != true) {
            throw new RuntimeException("This literal is not true, can not be applied");
        }
        if (!(l.getFormula() instanceof AppliableFormula)) {
            throw new RuntimeException("The formula in this literal is not appliable.");
        }
        AppliableFormula formula = (AppliableFormula) l.getFormula();
        apply(formula);
    }

    public void apply(List<Literal> ls) {
        for (Literal l : ls) {
            apply(l);
        }
    }

    public boolean eval(Literal l) {
        return l.getFormula().eval(this) == l.getState();
    }

    public boolean eval(List<Literal> ls) {
        for (Literal l : ls) {
            if (!eval(l)) {
                return false;
            }
        }
        return true;
    }

    public boolean eval(Formula formula) {
        return formula.eval(this);
    }

    @Override
    public MarsRoverModel clone() {
        MarsRoverModel cloneModel = new MarsRoverModel();
        cloneModel.curAgentPosition = curAgentPosition;
        cloneModel.preAgentPosition = preAgentPosition;
        cloneModel.agentFuel = agentFuel;
        return cloneModel;
    }
}
