package running;

import agent.AbstractAgent;
import agent.NFIFOAgent;
import agent.NMCTSAgent;
import agent.VBDIAgent;
import gpt.Position;
import graphic.EnvironmentDisplayer;
import org.junit.jupiter.api.Test;
import world.Environment;
import world.Norm;

import java.util.HashMap;
import java.util.List;

import static running.Default.*;

public class NormTest {
    @Test
    public double normAgentTest() {

        HashMap<Position, Norm> norms = genNorms(def_map_size, 47, 3, def_initial_Position, rm);
        List<Position> goals = Default.genGoals(def_map_size, 8, def_initial_Position, rm);
        AbstractAgent agent = new NFIFOAgent(goals, norms);
        agent = new VBDIAgent(goals, norms);
        agent = new NMCTSAgent(goals, norms);
        Environment environment = new Environment(agent);
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
    @Test
    public void speedTest() {
        int i = 0;
        long flipCount = 0;
        long beginTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - beginTime < 20000) {
            i++;
            System.out.println(i);
            if (i - 1 < 0 && i > 0) {
                flipCount++;
            } else if (i - 1 > 0 && i < 0) {
                flipCount++;
            }
        }
        System.out.println("flip count: " + flipCount);
        System.out.println(i);
    }
}
