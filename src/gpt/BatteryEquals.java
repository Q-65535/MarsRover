package gpt;

import world.*;

public class BatteryEquals implements Formula {
    // @Incomplete: For each term in a formula, we need to provide a function to get its instantiated value.
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

    public Num insTerm1(Environment model) {
        return testNum.ins(model);
    }
}
