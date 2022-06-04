package running;

import agent.AbstractAgent;
import agent.NFIFOAgent;
import graphic.EnvironmentDisplayer;
import org.junit.jupiter.api.Test;
import world.Cell;
import world.Environment;
import world.Norm;

import java.util.HashMap;
import java.util.Set;

import static running.Default.*;

public class NormTest {
    @Test
    public void normFIFO() {
        HashMap<Cell, Norm> norms = randomGenerateNorms(def_map_size, 10, def_initial_Position, rm);
        Set<Cell> goals = randomGenerateTargetPositions(def_map_size, 10, def_initial_Position, rm);
        AbstractAgent nfifoAgent = new NFIFOAgent(def_initial_Position, goals, norms);
        Environment environment = new Environment(def_map_size, def_recharge_position, nfifoAgent, def_act_consumption);
        EnvironmentDisplayer displayer = new EnvironmentDisplayer();

        boolean running = true;
        while (running) {
            running = environment.run();
            displayer.display(environment);
        }
        double totalPenalty = nfifoAgent.getTotalPenalty();
        System.out.println(totalPenalty);
    }

    @Test
    public void loopTest() {
        for (int i = 0; i < 10; i++) {
            normFIFO();
        }
    }
}
