package bc.external.robot.mro.scenarios;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Random;
import org.junit.Ignore;
import org.junit.Test;

public class MouseScrollWheel {

    @Ignore
    @Test
    public void testClickMouseWheel() throws Exception {
        new Robot().mousePress(InputEvent.BUTTON2_MASK);
        Thread.sleep((new Random().nextInt(60) + 10));
        new Robot().mouseRelease(InputEvent.BUTTON2_MASK);
    }

}
