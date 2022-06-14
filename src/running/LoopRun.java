package running;

public class LoopRun {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Main.main(null);
        }
        System.out.println("total default env resource consumption: " + Main.naiveTotal);
        System.out.println("total mcts env resource consumption: " + Main.mctsTotal);
    }

}
