package gpt;

import world.*;

public class BatteryEquals implements AppliableFormula {
    // @Note (Just for future modifications on Environment class) After each makeTrue function in the model, we need verify whether the formula is really made true.
    NumTerm testNum;

    public BatteryEquals(int testNum) {
        this.testNum = new Num(testNum);
    }

    public BatteryEquals(NumTerm testNum) {
        this.testNum = testNum;
    }

    @Override
    public boolean eval(MarsRoverModel marsRoverModel) {

        Num num = this.testNum.ins(marsRoverModel);

        return marsRoverModel.getAgentFuel() == num.getInt();
    }

    public Num insTerm1(MarsRoverModel model) {
        return testNum.ins(model);
    }

    @Override
    public void apply(MarsRoverModel marsRoverModel) {
	Num targetBattery = insTerm1(marsRoverModel);
	marsRoverModel.setAgentFuel(targetBattery.getInt());
    }
}
