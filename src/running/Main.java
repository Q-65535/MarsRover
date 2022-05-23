package running;

import agent.AbstractAgent;
import graphic.EnvironmentDisplayer;
import world.Environment;

public class Main {

    public static void main(String[] args) {
        EnvironmentDisplayer displayer = new EnvironmentDisplayer();
        Environment environment = Utils.defEnv;
        boolean running = true;
        while (running) {
            running = environment.run();
            displayer.display(environment);
        }
    }

}
