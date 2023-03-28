package running;

import world.*;
import static world.Calculator.*;

import java.util.*;

public class Default {
    public static int SEED = 1;
    public static Random rm = new Random(SEED);
    public static Random goalGenerateRM = new Random(SEED);
    public static final int def_map_size = 20;
    public static final int def_num_goals = 10;
    public static final int def_max_capacity = def_map_size * 2;
    public static final int def_act_consumption = 1;
    public static final Cell middle_Position = new Cell(def_map_size / 2, def_map_size / 2);
    public static final Cell def_initial_Position = middle_Position;
    public static final Cell def_recharge_position = middle_Position;

    public static List<Cell> def_goals;


    /**
     * given the map size and number of goals to generate, randomly generate a specific number of goal cells
     */
    public static List<Cell> genGoals(int mapSize, int numOfGoals, Cell except) {
        List<Cell> res = new ArrayList<>();
        while (res.size() < numOfGoals) {
            int x = goalGenerateRM.nextInt(mapSize);
            int y = goalGenerateRM.nextInt(mapSize);
            Cell cell = new Cell(x, y);

            // if the newly generated goal is at the forbidden position or already added to the list, continue
            if (except.equals(cell) || res.contains(cell)) {
                continue;
            }
            res.add(cell);
        }
        return res;
    }

    /**
     * Randomly generate a set of goal positions with an except location
     */
    public static List<Cell> genGoals(int mapSize, int numOfGoals, Cell except, Random rm) {
        List<Cell> res = new ArrayList<>();
        while (res.size() < numOfGoals) {
            int x = rm.nextInt(mapSize);
            int y = rm.nextInt(mapSize);
            Cell cell = new Cell(x, y);

            if (cell.equals(except) || res.contains(cell)) {
                continue;
            }
            res.add(cell);
        }
        return res;
    }

    public static HashMap<Cell, Norm> genNorms(int mapSize, int numOfNorms, double avgPenalty, Cell except, Random rm) {HashMap<Cell, Norm> norms = new HashMap<>();
        while (norms.size() < numOfNorms) {
            int x = rm.nextInt(mapSize);
            int y = rm.nextInt(mapSize);
            Cell cell = new Cell(x, y);
            if (cell.equals(except)) {
                continue;
            }
            double penaltyValue = gaussian(avgPenalty, 0.01, 0.01, rm);
            norms.put(cell, new Norm(cell, penaltyValue));
        }
        return norms;
    }

    /**
     * Generate a number according to normal distribution with specified mean, standard deviation and bounds
     */
    private static double gaussian(double mean, double stdDeviation, double rangeRadius, Random rm) {
        // the generated value
        double value = rm.nextGaussian() * stdDeviation + mean;
        // if the value is not in bounds, generate again and again
        while (value < mean - rangeRadius || value > mean + rangeRadius) {
            value = rm.nextGaussian() * stdDeviation + mean;
        }
        return value;
    }

	public static List<Boundary> genBoundaries(int mapSize, int boundaryCount, int boundaryLen, Random rm) {
		List<Boundary> res = new ArrayList<>();
		outer: while (res.size() < boundaryCount) {
			// First we need to determine whether the boundary is horizontal or vertical.
			boolean isHorizontal = rm.nextBoolean();
			// startCoordinate must be the smaller than endCoordinate and
			// endCoordinate cannot be out of map boundary.
			int startCoordinate = rm.nextInt(mapSize - boundaryLen);
			int endCoordinate = startCoordinate + boundaryLen;
			// Note that crossFrom cannot be the maximium coordinate in the map, thus mapSize - 1.
			int crossFrom = rm.nextInt(mapSize - 1);
			int crossTo = crossFrom + 1;
			// Let rm determine the cross direction (whether switch the "from" and "to" direction).
			if (rm.nextBoolean()) {
				int temp = crossFrom;
				crossFrom = crossTo;
				crossTo = temp;
			}
			// Avoid overlapping boundaries.
			for (Boundary boundary : res) {
				if (isHorizontal != boundary.isHorizontal) {
					continue;
				}
				if (endCoordinate <= boundary.endCoordinate && endCoordinate >= boundary.startCoordinate) {
					if (crossTo == boundary.crossTo || crossTo == boundary.crossFrom || crossFrom == boundary.crossFrom || crossFrom == boundary.crossTo) {
						continue outer;
					}
				}
				if (startCoordinate <= boundary.endCoordinate && startCoordinate >= boundary.startCoordinate) {
					if (crossTo == boundary.crossTo || crossTo == boundary.crossFrom || crossFrom == boundary.crossFrom || crossFrom == boundary.crossTo) {
						continue outer;
					}
				}
			}
			Boundary newBoundary = new Boundary(isHorizontal, startCoordinate, endCoordinate, crossFrom, crossTo);
			res.add(newBoundary);
		}
        return res;
	}

	public static List<Boundary> genMiddleBoundary(int mapSize) {
		List<Boundary> res = new ArrayList<>();
		int crossTo = mapSize / 2;
		int crossFrom = crossTo - 1;
		int endCoordinate = mapSize - 1;
		int startCoordinate = 0;
		// The boundary is vertical.
		Boundary newBoundary = new Boundary(false, startCoordinate, endCoordinate, crossFrom, crossTo);
		res.add(newBoundary);
		return res;
	}

	public static NormLands genMiddleLineShape(int mapSize) {
		Set<Cell> highlands = genVerticalLine(mapSize / 2 - 1, 0, mapSize);
		Set<Cell> lowlands = genVerticalLine(mapSize / 2, 0, mapSize);
		return new NormLands(highlands, lowlands);
	}

	public static NormLands genShapeCollection() {
		NormLands normLands = genUShapeSlope(3, 5, 5);
		normLands.extendNormLands(genLShapeSlope(15, 5, 5));
		normLands.extendNormLands(genHorizontalLineSlope(10, 15, 5));
		normLands.extendNormLands(genVerticalLineSlope(11, 1, 5));

		return normLands;

	}

	public static NormLands genLShapeSlope(int x, int y, int len) {
		Set<Cell> highlandCells = new HashSet<>();
		Set<Cell> lowlandCells = new HashSet<>();

		Set<Cell> horizontalHighlandCells = genHorizontalLine(x, y, len);
		Set<Cell> horizontalLowlandCells = genHorizontalLine(x + 1, y + 1, len - 1);
		Set<Cell> verticalHighlandCells = genVerticalLine(x, y, len);
		Set<Cell> verticalLowlandCells = genVerticalLine(x + 1, y + 1, len - 1);

		highlandCells.addAll(horizontalHighlandCells);
		highlandCells.addAll(verticalHighlandCells);
		lowlandCells.addAll(horizontalLowlandCells);
		lowlandCells.addAll(verticalLowlandCells);
		return new NormLands(highlandCells, lowlandCells);
	}

	public static NormLands genUShapeSlope(int x, int y, int len) {
		NormLands UShapeSlope = genLShapeSlope(x, y, len);

		Set<Cell> upperHorizontalHighlands = genHorizontalLine(x, y + len, len);
		// upperHorizontalHighlands.add(new Cell(x, y - 1));
		UShapeSlope.extendHighlands(upperHorizontalHighlands);

		Set<Cell> upperHorizontalLowlands = genHorizontalLine(x + 1, y + len - 1, len - 1);
		UShapeSlope.extendLowlands(upperHorizontalLowlands);
		return UShapeSlope;
	}

	public static NormLands genHorizontalLineSlope(int x, int y, int len) {
		Set<Cell> highlandCells = genHorizontalLine(x, y, len);
		Set<Cell> lowlandCells = genHorizontalLine(x, y + 1, len);

		return new NormLands(highlandCells, lowlandCells);
	}

	public static NormLands genVerticalLineSlope(int x, int y, int len) {
		Set<Cell> highlandCells = genVerticalLine(x, y, len);
		Set<Cell> lowlandCells = genVerticalLine(x + 1, y, len);

		return new NormLands(highlandCells, lowlandCells);
	}

	// The coordinate is at the down-left of the line.
	private static Set<Cell> genHorizontalLine(int x, int y, int len) {
		Set<Cell> highlandCells = new HashSet<>();
		for (int i = 0; i < len; i++) {
			highlandCells.add(new Cell(x + i, y));
		}
		return highlandCells;
	}

	private static Set<Cell> genVerticalLine(int x, int y, int len) {
		Set<Cell> highlandCells = new HashSet<>();
		for (int i = 0; i < len; i++) {
			highlandCells.add(new Cell(x, y + i));
		}
		return highlandCells;
	}



    public static List<Cell> cloneCells(List<Cell> cells) {
        List<Cell> cloneSet = new ArrayList<>();
        for (Cell cell : cells) {
            cloneSet.add(cell);
        }
        return cloneSet;
    }
}
