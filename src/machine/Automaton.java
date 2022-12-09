package machine;

import gpt.*;
import world.*;

import java.util.*;


public class Automaton {
    private final String name;
    public final int priority;

    private State initState;
    private State curState;
    private State preState;

    private Set<State> states;
    // Whether the last transition is succeed.
    // Initially, it is set to be true.
    private boolean transitionSucceed = true;


    public Automaton(String name, Set<State> states, State initState) {
	// The default priority of an automaton is 0.
	this(name, 0, states, initState);
    }

    public Automaton(String name, int priority, Set<State> states, State initState) {
        this.name = name;
	this.priority = priority;
        this.states = states;
        this.initState = initState;
        this.curState = initState;
        this.preState = initState;
    }

    public String getName() {
        return this.name;
    }

    public State getCurState() {
        return curState;
    }

    public State getPreState() {
        return preState;
    }

    public void setPreState(State s) {
        preState = s;
    }

    public Set<State> getStates() {
        return states;
    }

    public boolean isFinalTrap() {
	return curState.isFinalTrap();
    }

    public boolean isTransitionSucceed() {
        return transitionSucceed;
    }

    /**
     * Get the next target literal.
     */
    public Literal getNextTargetLiteral() {
	return curState.getTargetLiteral();
    }

    public boolean transit(MarsRoverModel model) {
        preState = curState;
        State nextState = curState.nextState(model);
        // @Incomplete: If no transition can be applied, we need trigger something.
        if (nextState == null) {
            System.out.println(curState + "has no transition rule to be applied for the given model");
            transitionSucceed = false;
            return false;
        } else {
            Literal transitionLiteral = getTransitionLiteral(curState, nextState);
            // System.out.println(curState + arrowLiteral(transitionLiteral) + nextState);
            this.curState = nextState;
            transitionSucceed = true;
            return true;
        }
    }

    // @Smell: If from is final trap, it will return null.
    public Literal getTransitionLiteral(State from, State to) {
        for (Map.Entry<Literal, State> pair : from.getTransFunc().entrySet()) {
            // @Robustness: Rewrite equals method?
            if (pair.getValue().equals(to)) {
                return pair.getKey();
            }
        }
        return null;
    }

    // Surround the given literal with arrow.
    private String arrowLiteral(Literal l) {
        return " ===" + l + "===> ";
    }


    private String arrowString(String s) {
        return " ===" + s + "===> ";
    }

    public boolean isAccept() {
        return curState.isFinalState();
    }

    @Override
    public Automaton clone() {
        Automaton cloneAutomaton = new Automaton(name, priority, new HashSet<>(states), initState);
        cloneAutomaton.curState = this.curState;
        cloneAutomaton.preState = this.preState;
        cloneAutomaton.transitionSucceed = transitionSucceed;
        return cloneAutomaton;
    }
}
