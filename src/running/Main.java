package running;

import agent.AbstractAgent;
import agent.FIFOAgent;
import generator.MarsRoverGenerator;
import generator.StateMachineGenerator;
import gpt.*;
import graphic.AutoDotWriter;
import graphic.EnvironmentDisplayer;
import machine.Automaton;
import world.Environment;

import java.io.*;
import java.util.*;

import static running.Default.*;

public class Main {
    static int naiveTotal = 0;
    static int mctsTotal = 0;

    public static String execCmd(String cmd) {
        String result = null;
        try (InputStream inputStream = Runtime.getRuntime().exec(cmd).getInputStream();
             Scanner s = new Scanner(inputStream).useDelimiter("\\A")) {
            result = s.hasNext() ? s.next() : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.printf("wer");
        Runtime rt = Runtime.getRuntime();
        try {
            String output = execCmd("ls");
        } catch (Exception e) {
            System.out.println("Command line execution error!");
        }


        EnvironmentDisplayer displayer = new EnvironmentDisplayer();
        Default.def_goals = Default.genGoals(def_map_size, def_num_goals, def_initial_Position, rm, 0);

        // AbstractAgent fifoagent = new FIFOAgent(def_max_capacity);
        AbstractAgent fifoagent = new FIFOAgent(40);
        MarsRoverGenerator genGPT = new MarsRoverGenerator();
	StateMachineGenerator genAuto = new StateMachineGenerator();

        // Creating and adding achievement goals automata.
        for (int i = 0; i < 30; i++) {
	    Automaton auto = genAuto.genBasicAchievementAuto(rm.nextInt(def_map_size), rm.nextInt(def_map_size));
	    fifoagent.getAutomata().add(auto);
        }
        // Add maintenance goal in the form of automaton.
        fifoagent.getAutomata().add(genAuto.genBasicMaitAuto(20));

	// Add norm automata
	for (int i = 0; i < 30; i++) {
	    int x = rm.nextInt(def_map_size);
	    int y = rm.nextInt(def_map_size);
	    Position from = new Position(x, y);
	    Position to = new Position(x + 1, y);

	    Automaton auto = genAuto.genBasicNormAuto(from, to, -0.1);
	    fifoagent.getAutomata().add(auto);
        fifoagent.addNormPosition(from, to);
	}

        Environment defEnv = new Environment(fifoagent);
        boolean running = true;
        defEnv.setAgent(fifoagent);
        while (running) {
            running = defEnv.run();
            displayer.display(defEnv);
            File dotDir = new File("/Users/wudi/automata_dots");
            AutoDotWriter autoDotWriter = new AutoDotWriter();
            List<Automaton> automata = fifoagent.getAutomata();

            System.out.printf("Current number of intentions: %d%n", fifoagent.getIntentions().size());
//            autoDotWriter.genTransDotFile(dotDir, automata);
//           pause(500);
//            autoDotWriter.genDotFile(dotDir, automata);
//           pause(500);
        }

        displayer.close();

        naiveTotal += fifoagent.getTotalFuelConsumption();
        mctsTotal += fifoagent.getTotalFuelConsumption();
        System.out.println("default env resource consumption: " + fifoagent.getTotalFuelConsumption());
        System.out.println("MCTS env resource consumption: " + fifoagent.getTotalFuelConsumption());
    }

    private static void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
