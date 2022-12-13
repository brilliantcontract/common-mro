package bc.external.robot.mro.scenarios;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

public class MCScenario implements Scenario {

    private final AdvRobot robot;
    private Integer sessionDuration = 0;
    private Integer startDelaySeconds = 10;
    private final Random random = new Random();
    private Integer eventsRate = 1;

    public Integer getStartDelaySeconds() {
        return this.startDelaySeconds;
    }

    @Override
    public void setStartDelaySeconds(Integer seconds) {
        this.startDelaySeconds = seconds;
    }

    public Integer getSessionDuration() {
        return this.sessionDuration;
    }

    @Override
    public void setSessionDuration(Integer seconds) {
        this.sessionDuration = seconds;
    }

    public MCScenario() throws AWTException {
        this.robot = new AdvRobot(new Robot());
    }

    @Override
    public void windOff() throws Exception {
        LocalDateTime startTime = LocalDateTime.now();

        for (int i = this.startDelaySeconds; i > 0; i--) {
            System.out.println("Start after " + i + " sec");
            Thread.sleep(1_000);
        }

        try {
            while (LocalDateTime.now().isBefore(startTime.plusSeconds(this.sessionDuration))) {
                this.eventsRate = this.random.nextInt(4) + 1;

                playWithWebBrowser();
//                this.robot.hotkey(new int[]{18, 9});
            }
        } finally {
            this.robot.beep(500, 5_000, 1.0);
        }
    }

    private void playWithWebBrowser() throws Exception {
        Thread.sleep(2000L);

        int iterationNumber = 0;
        while (iterationNumber < 40) {
            iterationNumber++;
            sleep(450, 2800);

            if (random.nextInt(4) == 2) {
                robot.moveMouseToArbitraryLocation(2000, 3800, 250, 1000);
                Thread.sleep((random.nextInt(500) + 150));
//                robot.mouseClickScrollWheelButton();
//                Thread.sleep((random.nextInt(500) + 150));
                robot.mouseWheel(-50);
                Thread.sleep((random.nextInt(500) + 250));
            }

            int randomNumber = (new Random()).nextInt(50);
            if (randomNumber >= 0 && randomNumber < 36) {
                this.robot.typeRandomAlpahaNumericCharacter(
                        Arrays.asList(new Integer[]{
                    KeyEvent.VK_O,
                    KeyEvent.VK_T,
                    KeyEvent.VK_S,
                    KeyEvent.VK_J,
                    KeyEvent.VK_A,
                    KeyEvent.VK_L,
                    KeyEvent.VK_P,
                    KeyEvent.VK_M,
                    KeyEvent.VK_W,
                    KeyEvent.VK_C
                }));
                continue;
            }
            switch (randomNumber) {
                case 36:
                    this.robot.type(new int[]{33});

                case 37:
                    this.robot.type(new int[]{33});

                case 38:
                    this.robot.type(new int[]{34});

                case 39:
                    this.robot.type(new int[]{145});

                case 40:
                    this.robot.type(new int[]{38});

                case 41:
                    this.robot.type(new int[]{40});

                case 42:
                    this.robot.type(new int[]{37});

                case 43:
                    this.robot.type(new int[]{39});

                case 44:
                    this.robot.type(new int[]{32});

                case 45:
                    this.robot.type(new int[]{33});

                case 46:
                case 47:
                case 48:
                case 49:
                    this.robot.hotkey(new int[]{17, 34});
            }
        }
    }

    private void sleep(int min, int max) throws InterruptedException {
        Thread.sleep(Math.round(((this.random.nextInt(max) + min) / this.eventsRate)));
    }
}
