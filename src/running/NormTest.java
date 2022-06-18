package running;

import agent.AbstractAgent;
import agent.NFIFOAgent;
import agent.NMCTSAgent;
import agent.VBDIAgent;
import graphic.EnvironmentDisplayer;
import org.junit.jupiter.api.Test;
import world.Cell;
import world.Environment;
import world.Norm;

import java.util.HashMap;
import java.util.List;

import static running.Default.*;

public class NormTest {
    @Test
    public double normAgentTest() {

        HashMap<Cell, Norm> norms = genNorms(def_map_size, 47, 3, def_initial_Position, rm);
        List<Cell> goals = Default.genGoals(def_map_size, 8, def_initial_Position, rm);
        AbstractAgent agent = new NFIFOAgent(def_initial_Position, goals, norms);
        agent = new VBDIAgent(def_initial_Position, goals, norms);
        agent = new NMCTSAgent(def_initial_Position, goals, norms);
        Environment environment = new Environment(def_map_size, def_recharge_position, agent, def_act_consumption);
        EnvironmentDisplayer displayer = new EnvironmentDisplayer();

        boolean running = true;
        while (running) {
            running = environment.run();
            displayer.display(environment);
        }
        double totalPenalty = agent.getTotalPenalty();
        System.out.println(totalPenalty);
        return totalPenalty;
    }

    @Test
    public void loopTest() {
        double penaltySum = 0;
        for (int i = 0; i < 50; i++) {
            penaltySum += normAgentTest();
        }
        System.out.println(penaltySum);
    }
}
