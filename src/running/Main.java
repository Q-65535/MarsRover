package running;

import agent.AbstractAgent;
import agent.FIFOAgent;
import agent.MCTSAgent;
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

//         AbstractAgent agent = new FIFOAgent(def_max_capacity);
//        AbstractAgent agent = new FIFOAgent(60);
        AbstractAgent agent = new MCTSAgent(60);
        StateMachineGenerator genAuto = new StateMachineGenerator();

        // Creating and adding achievement goals automata.
        for (int i = 0; i < 10; i++) {
            Automaton auto = genAuto.genBasicAchievementAuto(rm.nextInt(def_map_size), rm.nextInt(def_map_size));
            agent.getAutomata().add(auto);
        }
        // Add maintenance goal in the form of automaton.
        agent.getAutomata().add(genAuto.genBasicMaitAuto(20));

        // Add norm automata
//	for (int i = 0; i < 100; i++) {
//	    int x = rm.nextInt(def_map_size);
//	    int y = rm.nextInt(def_map_size);
//	    Position from = new Position(x, y);
//	    Position to = new Position(x + 1, y);
//
//	    Automaton auto = genAuto.genBasicNormAuto(from, to, -0.1);
//	    agent.getAutomata().add(auto);
//        agent.addNormPosition(from, to);
//	}

        Environment defEnv = new Environment(agent);
        boolean running = true;
        defEnv.setAgent(agent);
        while (running) {
            running = defEnv.run();
            displayer.display(defEnv);
            File dotDir = new File("/Users/wudi/automata_dots");
            AutoDotWriter autoDotWriter = new AutoDotWriter();
            List<Automaton> automata = agent.getAutomata();

            System.out.printf("Current number of intentions: %d%n", agent.getIntentions().size());
            autoDotWriter.genTransDotFile(dotDir, automata);
//           pause(500);
            autoDotWriter.genDotFile(dotDir, automata);
//           pause(500);
        }

        displayer.close();

        naiveTotal += agent.getTotalFuelConsumption();
        mctsTotal += agent.getTotalFuelConsumption();
        System.out.println("default env resource consumption: " + agent.getTotalFuelConsumption());
        System.out.println("MCTS env resource consumption: " + agent.getTotalFuelConsumption());
    }

    private static void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
