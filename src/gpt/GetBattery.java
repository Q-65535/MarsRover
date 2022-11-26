package gpt;

import world.*;
import agent.*;

public class GetBattery extends NumFunc {
    @Override
    public Num ins(Environment model) {
        AbstractAgent agent = model.getAgent();
        int battery = agent.getCurrentFuel();
        return new Num(battery);
    }
}
