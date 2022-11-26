package gpt;

import world.*;

public class Num implements NumTerm {
    private final int num;

    public Num(int num) {
        this.num = num;
    }

    @Override
    // @Smell: the input argument model is not used.
    public Num ins(Environment model) {
        return this;
    }

    public int getInt() {
        return num;
    }
}
