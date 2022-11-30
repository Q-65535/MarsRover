package gpt;

import world.MarsRoverModel;

/**
 * The Formula that can be applied to a model.
 * @Node All postconditions are of this type.
 */
public interface AppliableFormula extends Formula {
    /**
     * Apply this formula to the given model.
     */
    public abstract void apply(MarsRoverModel marsRoverModel);
}
