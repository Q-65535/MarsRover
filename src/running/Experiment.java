package running;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static running.Default.*;
import static running.Utils.*;

public class Experiment {
    public static String homeDir = System.getProperty("user.home");
    public static String RESULT_ROOT_DIR = join(homeDir, "MR_results_old").getPath();

    @Test
    void goalX_capY() {
		String resDir = join(RESULT_ROOT_DIR, "mg_goalX_capY").getPath();
		MGResultProducer resultProducer = new MGResultProducer(resDir);
		resultProducer.setGoalRange(1, 15, 1);
		resultProducer.setCapRange(2, 10, 20);
//        resultProducer.enableProduceFile();
//       resultProducer.enableDrawGraphic();

        // resultProducer.exp_goalX_capY("infbasic", capacity);
        // resultProducer.exp_goalX_capY("basic", capacity);
        // resultProducer.exp_goalX_capY("probasic", capacity);
        // resultProducer.exp_goalX_capY("infmcts", capacity);
        resultProducer.exp_goalX_capY("reamcts");
        resultProducer.exp_goalX_capY("promcts");
        // resultProducer.exp_goalX_capY("vbdi", capacity);
    }

    @Test
    void goalX_capY_fixInterval() {
		String resDir = join(RESULT_ROOT_DIR, "mg_goalX_capY").getPath();
		MGResultProducer resultProducer = new MGResultProducer(resDir);
		resultProducer.setGoalRange(1, 15, 1);
		resultProducer.setCapRange(2, 10, 20);
		resultProducer.setInterval(5);
//        resultProducer.enableProduceFile();
//        resultProducer.enableDrawGraphic();

//        resultProducer.exp_goalX_capY("reamcts");
//        resultProducer.exp_goalX_capY("fifo");
        resultProducer.exp_goalX_capY("vbdi");
    }

    @Test
    void intervalX_capY() {
		String resDir = join(RESULT_ROOT_DIR, "mg_intervalX_capY").getPath();
		MGResultProducer resultProducer = new MGResultProducer(resDir);
		resultProducer.setCapRange(2, 10, 20);
		resultProducer.setIntervalRange(1, 15, 1);
		resultProducer.exp_intervalX_capY("reamcts", 10);
		resultProducer.exp_intervalX_capY("fifo", 10);
    }









// ------------------------------------norm experiment------------------------------------

    @Test
    void intervalX_goalY() {
		String resDir = join(RESULT_ROOT_DIR, "sectorSetting_intervalX_goalY_onlyNorm").getPath();
		MGResultProducer resultProducer = new MGResultProducer(resDir);
		resultProducer.setGoalRange(1, 15, 1);
		 resultProducer.enableProduceFile();
//		resultProducer.enableDrawGraphic();

		resultProducer.norm_exp_intervalX_goalY("NMG");
		resultProducer.norm_exp_intervalX_goalY("vBDI");
		resultProducer.norm_exp_intervalX_goalY("NMCTS");
    }


// ------------------------------------state machine experiment------------------------------------

    @Test
    void machine_goalRow_capCol_varyInterval() {
		for (int interval = 0; interval <= 0; interval++) {
			String resDir = join(RESULT_ROOT_DIR, "oneLineBorder_goalRow_capCol_interval" + interval).getPath();
			MGResultProducer resultProducer = new MGResultProducer(resDir);
			resultProducer.setGoalRange(1, 15, 1);
			resultProducer.setCapRange(2, 10, 20);
			 resultProducer.enableProduceFile();
//			resultProducer.enableDrawGraphic();

			resultProducer.norm_exp_goalX_capY_varyInterval("PMG", interval);
			resultProducer.norm_exp_goalX_capY_varyInterval("vBDI", interval);
			resultProducer.norm_exp_goalX_capY_varyInterval("PMCTS", interval);
		}
    }

    @Test
    void machine_intervalRow_capCol_varyGoalCount() {
		for (int goalCount = 10; goalCount <= 10; goalCount++) {
			String resDir = join(RESULT_ROOT_DIR, "oneLineBorder_intervalRow_capCol_GoalCount_" + goalCount).getPath();
			MGResultProducer resultProducer = new MGResultProducer(resDir);
			resultProducer.setIntervalRange(0, 15, 1);
			resultProducer.setCapRange(2, 10, 20);
			 resultProducer.enableProduceFile();
//			resultProducer.enableDrawGraphic();

			resultProducer.norm_exp_intervalX_capacityY_varyGoalCount("PMG", goalCount);
			resultProducer.norm_exp_intervalX_capacityY_varyGoalCount("vBDI", goalCount);
			resultProducer.norm_exp_intervalX_capacityY_varyGoalCount("PMCTS", goalCount);
		}
    }





    @Test
    void testDirectoryString() {
        String homeDir = System.getProperty("user.home");
        System.out.println(homeDir);
    }
}
