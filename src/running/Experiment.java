package running;

import org.junit.jupiter.api.Test;
import static running.Default.*;
import static running.Utils.*;

public class Experiment {
    public static void main(String[] args) {
        ResultProducer resultProducer = new ResultProducer(join(RESULT_ROOT_DIR, "mg").getPath(), 10, 15);
//        resultProduce.expAgent("fifo", "fifo.txt");
        resultProducer.expAgent("profifo", "profifo_random2.txt");
//        resultProduce.expAgent("mcts", "mcts.txt");
    }

    @Test
    public void normResProduce() {
        NormResultProducer normResultProducer = new NormResultProducer(join(RESULT_ROOT_DIR, "norm").getPath(), 15, 10, 50);

//        normResultProducer.expAgentVaryNumOfNorms("vbdi", "vbdi.txt");
//        normResultProducer.expAgentVaryNumOfNorms("fifo", "fifo.txt");
        normResultProducer.expAgentVaryNumOfNorms("mcts", "mcts.txt");
    }
}
