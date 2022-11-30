package gpt;

import world.*;

public class GetDiffBattery extends NumFunc {
    private final NumTerm baseNum;
    private final int diff;

    public GetDiffBattery(NumTerm baseNum, int diff) {
        this.baseNum = baseNum;
        this.diff = diff;
    }

    @Override
    public Num ins(MarsRoverModel marsRoverModel) {
        Num num = baseNum.ins(marsRoverModel);
        int numInt = num.getInt();
        return new Num(numInt + diff);
    }
}
