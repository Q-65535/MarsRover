package agent;

public enum MoveAction {

    LEFT("left"),
    RIGHT("right"),
    UP("up"),
    DOWN("down");

    private String name;
    MoveAction(String name) {
        this.name = name;
    }
}
