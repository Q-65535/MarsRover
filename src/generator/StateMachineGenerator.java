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
        State init = new State("init"+ "_"+getRandomSubstr(alphabet, 2));
        State fin = new State("final"+ "_"+getRandomSubstr(alphabet, 2));
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

        return new Automaton(l.toString(), states, init);
    }




    static String getRandomSubstr(String s, int len) {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < len; i++) {
	    sb.append(alphabet.charAt(rm.nextInt(alphabet.length())));
	}
	return sb.toString();
    }
}
