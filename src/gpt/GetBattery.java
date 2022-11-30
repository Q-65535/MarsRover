package gpt;

import world.*;

public class GetBattery extends NumFunc {
    @Override
    public Num ins(MarsRoverModel marsRoverModel) {
        int battery = marsRoverModel.getAgentFuel();
        return new Num(battery);
    }
}
