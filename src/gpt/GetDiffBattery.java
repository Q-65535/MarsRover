package gpt;

import world.*;

public class GetDiffBattery extends NumFunc {
    private final Num baseNum;
    private final int diff;

    public GetDiffBattery(Num baseNum, int diff) {
        this.baseNum = baseNum;
        this.diff = diff;
    }

    @Override
    public Num ins(Environment model) {
        Num num = baseNum.ins(model);
        int numInt = num.getInt();
        return new Num(numInt + diff);
    }
}
