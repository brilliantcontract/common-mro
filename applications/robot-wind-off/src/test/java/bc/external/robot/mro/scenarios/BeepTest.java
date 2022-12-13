package bc.external.robot.mro.scenarios;

import java.awt.Robot;
import org.junit.Ignore;
import org.junit.Test;

public class BeepTest {

    @Ignore
    @Test
    public void test() throws Exception {
        new AdvRobot(new Robot()).beep(500, 5_000, 1.0);
    }
    
}