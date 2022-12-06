package machine;

import gpt.Literal;
import world.*;

import java.util.*;

public class State {
    private final String name;
    private Map<Literal, State> transFunc;
    // The target literal can cause transition towards final state.
    private Literal targetLiteral;
    private boolean isFinalState;
    private boolean isFinalTrap;

    public State(String name, Map<Literal, State> transFunc) {
        this.name = name;
        this.transFunc = transFunc;
    }

    public State(String name) {
        this.name = name;
        this.transFunc = new HashMap<>();
    }

    public String getName(){
        return this.name;
    }


    public void addTransRule(Literal l, State next) {
        transFunc.put(l, next);
    }

    public void addTargetTransRule(Literal targetLiteral, State next) {
	transFunc.put(targetLiteral, next);
	this.targetLiteral = targetLiteral;
    }

    public Map<Literal, State> getTransFunc() {
	return transFunc;
    }

    /**
     * Get the next state according to the given literal.
     */ public State nextState(Literal l) {
	if (isFinalTrap) {
	    return this;
	}
        return transFunc.get(l);
    }

    /**
     * Get the next state according to the given Model.
     */
    public State nextState(MarsRoverModel model) {
	if (isFinalTrap) {
	    return this;
	}
        for (Map.Entry<Literal, State> pair : transFunc.entrySet()) {
            if (model.eval(pair.getKey())) {
                return pair.getValue();
            }
        }
        // If no transition can be applied, return null.
        return null;
    }

    public void setTargetLiteral(Literal targetLiteral) {
	this.targetLiteral = targetLiteral;
    }

    public Literal getTargetLiteral() {
	return this.targetLiteral;
    }

    public void setAsFinalState() {
	isFinalState = true;
    }

    public boolean isFinalState() {
	return isFinalState;
    }

    public void setAsFinalTrap() {
	isFinalTrap = true;
    }

    public boolean isFinalTrap() {
	return isFinalTrap;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
