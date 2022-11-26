package agent;

public enum Direction {


    LEFT("left"),
    RIGHT("right"),
    UP("up"),
    DOWN("down");

    private String name;
    Direction(String name) {
        this.name = name;
    }

}
