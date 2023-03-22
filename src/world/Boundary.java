package world;

import java.util.*;

public class Boundary {
	public final boolean isHorizontal;

	// This determines the length of the border.
	public final int startCoordinate;
	public final int endCoordinate;

	// @Note: The agent is allowed to cross from "from" to "to", otherwise, it gets punished.
	// And crossFrom must be adjecent to crossTo.
	public final int crossFrom;
	public final int crossTo;

	public final Set<Cell> fromCells;
	public final Set<Cell> toCells;

	public Boundary(boolean isHorizontal, int startCoordinate, int endCoordinate, int crossFrom, int crossTo) {
		this.isHorizontal = isHorizontal;
		this.startCoordinate = startCoordinate;
		this.endCoordinate = endCoordinate;
		this.crossFrom = crossFrom;
		this.crossTo = crossTo;

		fromCells = new HashSet<>();
		for (int x = startCoordinate; x <= endCoordinate; x++) {
			int y = crossFrom;
			if (this.isHorizontal) {
				fromCells.add(new Cell(x, y));
			} else {
				fromCells.add(new Cell(y, x));
			}
		}
		toCells = new HashSet<>();
		for (int x = startCoordinate; x <= endCoordinate; x++) {
			int y = crossTo;
			if (this.isHorizontal) {
				toCells.add(new Cell(x, y));
			} else {
				toCells.add(new Cell(y, x));
			}
		}
	}
}
