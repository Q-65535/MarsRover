package agent;

public enum Sector {
	one("sector one"),
	two("sector two"),
	three("sector three"),
	four("sector four");

	private String name;
	Sector(String name) {
		this.name = name;
	}
}
