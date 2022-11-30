package gpt;

import world.*;

public class BatteryAbove implements Formula {
    // @Robutness: implement this as a term, so it can be instantiated according to a given model.
    int testNum;

    public BatteryAbove(int testNum) {
        this.testNum = testNum;
    }

    public boolean eval(MarsRoverModel marsRoverModel) {
        int currentBattery = marsRoverModel.getAgentFuel();
        return currentBattery > testNum;
    }
}
