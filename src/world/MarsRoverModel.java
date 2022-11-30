package world;

import gpt.*;

import java.util.*;

// @Smell: Should we implement a Model interface (or abstract class)?
public class MarsRoverModel {
    private Position agentPosition;
    private int agentFuel;

    public MarsRoverModel() {}

    public MarsRoverModel(Position currentPosition, int currentFuel) {
        this.agentPosition = currentPosition;
        this.agentFuel = currentFuel;
    }

    public Position getAgentPosition() {
        return agentPosition;
    }

    public int getAgentFuel() {
        return this.agentFuel;
    }

    // Update this model according to the given model
    public void sync(MarsRoverModel marsRoverModel) {
        setAgentPosition(marsRoverModel.getAgentPosition());
        setAgentFuel(marsRoverModel.getAgentFuel());
    }

    public void setAgentPosition(Position p) {
        this.agentPosition = p;
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
}
