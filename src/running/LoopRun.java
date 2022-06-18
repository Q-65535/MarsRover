package running;

public class LoopRun {
    public static double epsilon = 1e-6;
    public static void main(String[] args) {
        int repetition = 50;
        for (int i = 0; i < repetition; i++) {
            Main.main(null);
        }
        System.out.println("avg default env resource consumption: " + Main.naiveTotal / (repetition + epsilon));
        System.out.println("avg mcts env resource consumption: " + Main.mctsTotal / (repetition + epsilon));
    }

}
