package gpt;

import world.*;

/**
 * Calculate the distance between two positions.
 */
class GetDistance extends NumFunc {
    PositionTerm term1;
    PositionTerm term2;

    public GetDistance(PositionTerm term1, PositionTerm term2) {
        this.term1 = term1;
        this.term2 = term2;
    }

    /**
     * Calculate the distance between two positions.
     */
    @Override
    public Num ins(MarsRoverModel marsRoverModel) {
	Position p1 = term1.ins(marsRoverModel);
	Position p2 = term2.ins(marsRoverModel);

	int xdiff = Math.abs(p1.x - p2.x);
	int ydiff = Math.abs(p1.y - p2.y);
	return new Num(xdiff + ydiff);
    }

}
