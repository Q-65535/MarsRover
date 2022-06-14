package running;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static running.Default.*;
import static running.Utils.*;

public class Experiment {
    public static String RESULT_ROOT_DIR = "C:\\Users\\GB\\Documents\\projects\\res_results\\MS_results";
    @Test
    void mgResultProduce() {
        MGResultProducer resultProducer = new MGResultProducer(join(RESULT_ROOT_DIR, "mg").getPath(), 10, 15);
        resultProducer.expAgent("fifo", "fifo.txt");
        resultProducer.expAgent("profifo", "profifo.txt");
        resultProducer.expAgent("mcts", "mcts.txt");
//        resultProducer.expAgent("spmcts", "spmcts.txt");
    }

    @Test
    public void normVaryNumberResultProduce() {
        NormResultProducer normResultProducer = new NormResultProducer(join(RESULT_ROOT_DIR, "norm").getPath(), 15, 10, 50);
        normResultProducer.expAgentVaryNumOfNorms("vbdi", "vary_number_vbdi.txt");
        normResultProducer.expAgentVaryNumOfNorms("fifo", "vary_number_fifo.txt");
        normResultProducer.expAgentVaryNumOfNorms("mcts", "vary_number_mcts.txt");
//        normResultProducer.expAgentVaryNumOfNorms("spmcts", "vary_number_spmcts.txt");
    }





    // this experiment is currently not considered
    @Test
    void normVaryPenaltyResultProduce() {
        NormResultProducer normResultProducer = new NormResultProducer(join(RESULT_ROOT_DIR, "norm").getPath(), 15, 10, 50);
        normResultProducer.expAgentVaryNormPenalty("vbdi", "vary_penalty_vbdi.txt");
        normResultProducer.expAgentVaryNormPenalty("fifo", "vary_penalty_fifo.txt");
        normResultProducer.expAgentVaryNormPenalty("mcts", "vary_penalty_mcts.txt");
//        normResultProducer.expAgentVaryNormPenalty("spmcts", "vary_penalty_spmcts.txt");
    }
    @Test
    public void randomSeedTest() throws InterruptedException {
        Random r1 = new Random(SEED);
        Random r2 = new Random(SEED);
        r1.nextDouble();
        System.out.println(r1.nextInt());
        System.out.println(r2.nextInt());
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("------------");
            System.out.println(r1.nextInt());
            System.out.println(r2.nextInt());
        }
    }
}
