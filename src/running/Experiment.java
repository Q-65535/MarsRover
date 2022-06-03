package running;

public class Experiment {
    public static void main(String[] args) {
        ResultProducer resultProducer = new ResultProducer();
//        resultProduce.expAgent("fifo", "fifo.txt");
        resultProducer.expAgent("profifo", "profifo_random2.txt");
//        resultProduce.expAgent("mcts", "mcts.txt");
//        resultProduce.expAgent("greedy", "greedy.txt");
    }
}
