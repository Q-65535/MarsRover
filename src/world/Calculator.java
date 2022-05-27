package world;

public class Calculator {
    /**
     * Calculate the distance between any to cells
     */
    public static int calculateDistance(Cell from, Cell to) {
        int xDiff = Math.abs(from.getX() - to.getX());
        int yDiff = Math.abs(from.getY() - to.getY());
        return xDiff + yDiff;
    }
}
