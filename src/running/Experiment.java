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
    void goalX_normY_static() {
		String resDir = join(RESULT_ROOT_DIR, "sectorSetting_goalX_normY_infCap_static").getPath();
		MGResultProducer resultProducer = new MGResultProducer(resDir);
//        resultProducer.setInterval(5);
        resultProducer.setGoalRange(1, 15, 1);
//        resultProducer.setNormRange(0, 5, 10);

        // resultProducer.enableProduceFile();
       resultProducer.enableDrawGraphic();

        // resultProducer.exp_goalX_normY("NMG", MGResultProducer.infCapacity);
        // resultProducer.exp_goalX_normY("vBDI", MGResultProducer.infCapacity);
        resultProducer.exp_goalX_normY("NMCTS", MGResultProducer.infCapacity);
    }

    @Test
    void goalX_normY(int interval) {
		String resDir = join(RESULT_ROOT_DIR, "goalX_normY_infCap_dynamic").getPath();
		MGResultProducer resultProducer = new MGResultProducer(resDir);
       resultProducer.setInterval(interval);
        resultProducer.setGoalRange(1, 15, 1);
        resultProducer.setNormRange(0, 5, 10);

        resultProducer.enableProduceFile();
//        resultProducer.enableDrawGraphic();

        resultProducer.exp_goalX_normY("NMG", MGResultProducer.infCapacity);
        resultProducer.exp_goalX_normY("NMCTS", MGResultProducer.infCapacity);
        resultProducer.exp_goalX_normY("vBDI", MGResultProducer.infCapacity);
    }


// ------------------------------------state machine experiment------------------------------------
    @Test
    void goalX_normY_static(int capacity) {
		String resDir = join(RESULT_ROOT_DIR, "goalX_normY_fixCap" + capacity + "_static").getPath();
		MGResultProducer resultProducer = new MGResultProducer(resDir);
//        resultProducer.setInterval(5);
        resultProducer.setGoalRange(1, 15, 1);
        resultProducer.setNormRange(0, 5, 10);

        resultProducer.enableProduceFile();
//        resultProducer.enableDrawGraphic();

        resultProducer.exp_goalX_normY("NMG", capacity);
        resultProducer.exp_goalX_normY("RMG", capacity);
        resultProducer.exp_goalX_normY("PMG", capacity);
        resultProducer.exp_goalX_normY("NMCTS", capacity);
        resultProducer.exp_goalX_normY("RMCTS", capacity);
        resultProducer.exp_goalX_normY("PMCTS", capacity);
        resultProducer.exp_goalX_normY("vBDI", capacity);
    }

    @Test
    void goalX_normY_dynamic(int capacity) {
		String resDir = join(RESULT_ROOT_DIR, "goalX_normY_fixCap" + capacity + "_dynamic").getPath();
		MGResultProducer resultProducer = new MGResultProducer(resDir);
       resultProducer.setInterval(1);
        resultProducer.setGoalRange(1, 15, 1);
        resultProducer.setNormRange(0, 5, 10);

        // resultProducer.enableProduceFile();
       resultProducer.enableDrawGraphic();

        // resultProducer.exp_goalX_normY("NMG", capacity);
        // resultProducer.exp_goalX_normY("RMG", capacity);
        resultProducer.exp_goalX_normY("PMG", capacity);
        // resultProducer.exp_goalX_normY("NMCTS", capacity);
        // resultProducer.exp_goalX_normY("RMCTS", capacity);
        resultProducer.exp_goalX_normY("PMCTS", capacity);
        resultProducer.exp_goalX_normY("vBDI", capacity);
    }

    @Test
    void goalX_intervalY_norm(int interval) {
		String resDir = join(RESULT_ROOT_DIR, "goalX_normY_interval" + interval).getPath();
		MGResultProducer resultProducer = new MGResultProducer(resDir);
        resultProducer.setInterval(interval);
		resultProducer.setCapRange(2, 10, 20);
        resultProducer.setGoalRange(1, 15, 1);
        resultProducer.setNormRange(0, 5, 10);

        resultProducer.enableProduceFile();
       // resultProducer.enableDrawGraphic();

        // resultProducer.exp_goalX_normY("NMG", capacity);
        // resultProducer.exp_goalX_normY("RMG", capacity);
        resultProducer.exp_goalX_normY("PMG");
        // resultProducer.exp_goalX_normY("NMCTS", capacity);
        // resultProducer.exp_goalX_normY("RMCTS", capacity);
        resultProducer.exp_goalX_normY("vBDI");
        resultProducer.exp_goalX_normY("PMCTS");
    }

    @Test
    void goalX_normY_fix_cap_static() {
        for (int capacity = 40; capacity <= 200; capacity += 20) {
            goalX_normY_static(capacity);
        }
    }

    @Test
    void goalX_normY_fix_cap_dynamic() {
        for (int capacity = 40; capacity <= 200; capacity += 20) {
            goalX_normY_dynamic(capacity);
        }
    }

    @Test
    void goalX_normY_fix_cap_varyInterval() {
		for (int interval = 1; interval <= 15; interval++) {
			goalX_normY_dynamic(interval);
		}
    }

    @Test
    void testDirectoryString() {
        String homeDir = System.getProperty("user.home");
        System.out.println(homeDir);
    }
}
