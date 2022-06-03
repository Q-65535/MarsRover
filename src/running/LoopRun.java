package running;

public class LoopRun {
    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            Main.main(null);
        }
        System.out.println("total default env resource consumption: " + Main.defEnvTotal);
        System.out.println("total mcts env resource consumption: " + Main.mctsEnvTotal);
    }

}
