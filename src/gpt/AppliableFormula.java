package gpt;

import world.MarsRoverModel;

/**
 * The Formula that can be applied to a model.
 * @Node All postconditions are of this type.
 */
public interface AppliableFormula {
    /**
     * Apply this formula to the given mdoel.
     */
    public abstract void apply(MarsRoverModel marsRoverModel);
}
