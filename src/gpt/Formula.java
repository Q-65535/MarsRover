package gpt;

import world.*;

public interface Formula {
    public abstract boolean eval(Environment model);
}
