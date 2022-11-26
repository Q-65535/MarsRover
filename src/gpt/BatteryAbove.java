package gpt;

import world.*;
import agent.*;

public class BatteryAbove implements Formula {
    // @Robutness: implement this as a term, so it can be instantiated according to a given model.
    int testNum;

    public BatteryAbove(int testNum) {
        this.testNum = testNum;
    }

    public boolean eval(Environment model) {
        AbstractAgent agent = model.getAgent();
        int currentBattery = agent.getCurrentFuel();
        return currentBattery > testNum;
    }
}
