package generator;

import org.junit.jupiter.api.Test;
import gpt.*;

public class GeneratorTest {
    @Test
    public void testFile() {
        MarsRoverGenerator mrg = new MarsRoverGenerator();
        GoalNode goal = mrg.generate(3, 5);
        System.out.println("hello");
    }
}
