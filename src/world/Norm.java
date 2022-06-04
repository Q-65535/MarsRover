package world;

public class Norm {
    private final Cell cell;
    private final double penalty;

    public Norm(Cell cell, double penalty) {
        this.cell = cell;
        this.penalty = penalty;
    }

    public Cell getTargetCell() {
        return cell;
    }

    public double getPenalty() {
        return penalty;
    }
}
