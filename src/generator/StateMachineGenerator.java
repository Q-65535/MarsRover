package generator;

import java.util.*;

import gpt.*;
import machine.*;

public class StateMachineGenerator {
    static final String alphabet = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static Random rm = new Random();

    public Automaton genBasicAchievementAuto(int x, int y) {
        Formula formula = new At(x, y);
        Literal targetLiteral = new Literal(formula, true);
        return genBasicAchievementAuto(targetLiteral);
    }

    public Automaton genBasicAchievementAuto(Literal l) {
        // @Test: add random number to name
        State init = new State("init_"+ getRandomSubstr(2));
	// Final state has value of 1.
        State fin = new State("final_" + getRandomSubstr(2), 1);
        HashSet<State> states = new HashSet<>();
        fin.setAsFinalState();
        fin.setAsFinalTrap();
        states.add(init);
        states.add(fin);

        init.addTargetTransRule(l, fin);

        Literal cycleLiteral = l.genNegation();

        init.addTransRule(cycleLiteral, init);
	// The name of the state machine is the target literal (for achievement goal, it is).
        return new Automaton(l.toString(), states, init);
    }

    public Automaton genBasicMaitAuto(int batteryLevel) {

        Formula formula = new BatteryAbove(batteryLevel);
        Literal targetLiteral = new Literal(formula, true);
        return genBasicMaitAuto(targetLiteral);
    }

    public Automaton genBasicMaitAuto(Literal l) {
	// generate new state nodes.
        State init = new State("init");
	State unsatisfy = new State("unsatisfy");
        HashSet<State> states = new HashSet<>();
        states.add(init);
	states.add(unsatisfy);

	// State signature.
        init.setAsFinalState();

	// Set connections between state nodes.
        init.addTransRule(l, init);
	Literal neg = l.genNegation();
	init.addTransRule(neg, unsatisfy);

	unsatisfy.addTransRule(neg, unsatisfy);
	unsatisfy.addTargetTransRule(l, init);

	// Maintenance goal has higher priority.
        return new Automaton(l.toString(), 1, states, init);
    }

    /**
     * Generate norm automaton based on given from and to positions. The penalty must be non-positive
     * number.
     */
    public Automaton genBasicNormAuto(Position from, Position to, double penalty) {
	Formula f = new Cross(from, to);
	Literal violationLiteral = new Literal(f, true);
	Literal obeyLiteral = violationLiteral.genNegation();

	State init = new State("obey_" + getRandomSubstr(2));
	State violate = new State("violate_" + getRandomSubstr(2), penalty);
        HashSet<State> states = new HashSet<>();
        states.add(init);
	states.add(violate);

	init.addTransRule(obeyLiteral, init);
	init.addTransRule(violationLiteral, violate);
	violate.addTransRule(obeyLiteral, init);
	violate.addTransRule(violationLiteral, violate);

	return new Automaton(obeyLiteral.toString(), 0, states, init);
    }

    static String getRandomSubstr(int len) {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < len; i++) {
	    sb.append(alphabet.charAt(rm.nextInt(alphabet.length())));
	}
	return sb.toString();
    }
}
