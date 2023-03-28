package world;

import java.util.*;

public class NormLands {
	public Set<Cell> lowlands;
	public Set<Cell> highlands;

	public NormLands(Set<Cell> highlands, Set<Cell> lowlands) {
		this.highlands = new HashSet<>(highlands);
		this.lowlands = new HashSet<>(lowlands);
	}

	public void extendNormLands(NormLands lands) {
		this.lowlands.addAll(lands.lowlands);
		this.highlands.addAll(lands.highlands);
	}

	public void extendHighlands(Set<Cell> highlands) {
		this.highlands.addAll(highlands);
	}
	public void extendLowlands(Set<Cell> lowlands) {
		this.lowlands.addAll(lowlands);
	}
}
