package running;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static running.Default.*;
import static running.Utils.*;

public class Experiment {
    public static String RESULT_ROOT_DIR = "C:\\Users\\GB\\Documents\\projects\\res_results\\MR_results";

    @Test
    void mgVaryGoalNumberCapacityConfigInterval() {
        MGResultProducer resultProducer = new MGResultProducer(join(RESULT_ROOT_DIR, "100avg_mg_goalX_capY_fixInterval5").getPath(), 10, 15, 15, 5);
//        resultProducer.expAgentVaryGoalCapacity("inffifo", "NMG.txt");
        resultProducer.expAgentVaryGoalCapacity("fifo", "RMG.txt");
//        resultProducer.expAgentVaryGoalCapacity("profifo", "PMG.txt");
//        resultProducer.expAgentVaryGoalCapacity("infmcts", "NMCTS.txt");
        resultProducer.expAgentVaryGoalCapacity("reamcts", "RMCTS.txt");
//        resultProducer.expAgentVaryGoalCapacity("promcts", "PMCTS.txt");

//        resultProducer.expAgentVaryGoalCapacity("greedy", "greedy.txt");
//        resultProducer.expAgent("spmcts", "spmcts.txt");
    }
    @Test
    void mgVaryGoalNumberCapacity() {
        MGResultProducer resultProducer = new MGResultProducer(join(RESULT_ROOT_DIR, "100avg_mg_goalX_capY").getPath(), 10, 15, 15, 0);
//        resultProducer.expAgentVaryGoalCapacity("inffifo", "NMG.txt");
        resultProducer.expAgentVaryGoalCapacity("fifo", "RMG.txt");
//        resultProducer.expAgentVaryGoalCapacity("profifo", "PMG.txt");
//        resultProducer.expAgentVaryGoalCapacity("infmcts", "NMCTS.txt");
        resultProducer.expAgentVaryGoalCapacity("reamcts", "RMCTS.txt");
//        resultProducer.expAgentVaryGoalCapacity("promcts", "PMCTS.txt");

//        resultProducer.expAgentVaryGoalCapacity("greedy", "greedy.txt");
//        resultProducer.expAgent("spmcts", "spmcts.txt");
    }

    @Test
    void mgVaryTimeGapCapacity() {
        MGResultProducer resultProducer = new MGResultProducer(join(RESULT_ROOT_DIR, "100avg_mg_intervalX_capY").getPath(), 10, 15, 15, 0);
//        resultProducer.expAgentVaryTimeGapCapacity("inffifo", "NMG.txt");
        resultProducer.expAgentVaryTimeGapCapacity("fifo", "RMG.txt");
//        resultProducer.expAgentVaryTimeGapCapacity("profifo", "PMG.txt");
//        resultProducer.expAgentVaryTimeGapCapacity("infmcts", "NMCTS.txt");
        resultProducer.expAgentVaryTimeGapCapacity("reamcts", "RMCTS.txt");
//        resultProducer.expAgentVaryTimeGapCapacity("promcts", "PMCTS.txt");

//        resultProducer.expAgentVaryTimeGapCapacity("greedy", "timeGap_capacity_greedy.txt");
    }











    @Test
    public void normVaryNumberResultProduce() {
        NormResultProducer normResultProducer = new NormResultProducer(join(RESULT_ROOT_DIR, "norm").getPath(), 15, 10, 10, 0);
        normResultProducer.expAgentVaryNumOfNorms("fifo", "vary_norms_fifo.txt");
        normResultProducer.expAgentVaryNumOfNorms("vbdi", "vary_norms_vbdi.txt");
        normResultProducer.expAgentVaryNumOfNorms("mcts", "vary_norms_mcts.txt");
//        normResultProducer.expAgentVaryNumOfNorms("spmcts", "vary_number_spmcts.txt");
    }

    @Test
    public void dynamicNormVaryNumberResultProduce() {
        NormResultProducer normResultProducer = new NormResultProducer(join(RESULT_ROOT_DIR, "norm").getPath(), 15, 10, 10, 5);
        normResultProducer.expAgentVaryNumOfNorms("fifo", "vary_norms_fifo.txt");
        normResultProducer.expAgentVaryNumOfNorms("vbdi", "vary_norms_vbdi.txt");
        normResultProducer.expAgentVaryNumOfNorms("mcts", "vary_norms_mcts.txt");
//        normResultProducer.expAgentVaryNumOfNorms("spmcts", "vary_number_spmcts.txt");
    }

    @Test
    public void normVaryIntervalProduce() {
        NormResultProducer normResultProducer = new NormResultProducer(join(RESULT_ROOT_DIR, "norm").getPath(), 15, 10, 10, 15, 0);
        normResultProducer.expAgentVaryTimeGapCapacity("fifo", "vary_interval_fifo.txt");
        normResultProducer.expAgentVaryTimeGapCapacity("vbdi", "vary_interval_vbdi.txt");
        normResultProducer.expAgentVaryTimeGapCapacity("mcts", "vary_interval_mcts.txt");
//        normResultProducer.expAgentVaryNumOfNorms("spmcts", "vary_number_spmcts.txt");
    }





    // this experiment is currently not considered
//    @Test
//    void normVaryPenaltyResultProduce() {
//        NormResultProducer normResultProducer = new NormResultProducer(join(RESULT_ROOT_DIR, "norm").getPath(), 15, 10, 50);
//        normResultProducer.expAgentVaryNormPenalty("vbdi", "vary_penalty_vbdi.txt");
//        normResultProducer.expAgentVaryNormPenalty("fifo", "vary_penalty_fifo.txt");
//        normResultProducer.expAgentVaryNormPenalty("mcts", "vary_penalty_mcts.txt");
////        normResultProducer.expAgentVaryNormPenalty("spmcts", "vary_penalty_spmcts.txt");
//    }






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
    @Test
    public void cacheSpeedTest() {
        int len = 69999999;
        class ListNode {
            public ListNode next;
            public int val;
            public ListNode(int val, ListNode next) {
                this.val = val;
                this.next = next;
            }
            public ListNode(int val) {
                this.val = val;
            }
        }
        // Construct linked list
        int count = 0;
        ListNode head = new ListNode(count++);
        ListNode cur = head;
        while (count < len) {
            cur.next = new ListNode(count++);
            cur = cur.next;
        }

        // Construct array list
        ListNode[] arr = new ListNode[len];
        for (int i = 0; i < len; i++) {
            arr[i] = new ListNode(i);
        }

        long linkSum = 0;
        cur = head;
        // Record time
        long linkBegin = System.currentTimeMillis();
        while (cur != null) {
            linkSum += cur.val;
            cur = cur.next;
        }
        long linkEnd = System.currentTimeMillis();
        System.out.println("linked list sum: " + linkSum);
        System.out.println("linked list time consumed " + (linkEnd - linkBegin));
        long arrSum = 0;
        // record time
        long arrBegin = System.currentTimeMillis();
        for (int i = 0; i < len; i++) {
            arrSum += arr[i].val;
        }
        long arrEnd = System.currentTimeMillis();
        System.out.println("array sum: " + arrSum);
        System.out.println("array time consumed " + (arrEnd - arrBegin));
    }
}
