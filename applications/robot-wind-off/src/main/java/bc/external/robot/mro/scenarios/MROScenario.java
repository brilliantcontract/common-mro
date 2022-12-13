package bc.external.robot.mro.scenarios;

import java.awt.AWTException;
import java.awt.Robot;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class MROScenario implements Scenario {

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

    public MROScenario() throws AWTException {
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

                playWithPdf();
                this.robot.hotkey(new int[]{18, 9});

                playWithSheet();
                this.robot.hotkey(new int[]{18, 9});
                Thread.sleep(1000L);
                this.robot.hotkey(new int[]{17, 18, 40});

                playWithWebBrowser();
                this.robot.hotkey(new int[]{18, 9});

                playWithSQLDeveloper();
                this.robot.hotkey(new int[]{18, 9});
                Thread.sleep(1000L);
                this.robot.hotkey(new int[]{17, 18, 38});
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
            sleep(100, 2400);

            if (this.random.nextInt(4) == 2) {
                this.robot.moveMouseToArbitraryLocation(100, 1000, 100, 1000);
                Thread.sleep((this.random.nextInt(500) + 50));
            }

            int randomNumber = (new Random()).nextInt(50);
            if (randomNumber >= 0 && randomNumber < 36) {
                this.robot.typeRandomAlpahaNumericCharacter(new ArrayList<>());
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

    private void playWithSheet() throws Exception {
        Thread.sleep(2000L);

        int iterationNumber = 0;
        while (iterationNumber < 20) {
            iterationNumber++;
            sleep(100, 4900);

            if (this.random.nextInt(3) == 2) {
                this.robot.moveMouseToArbitraryLocation(100, 1000, 100, 1000);
                Thread.sleep((this.random.nextInt(500) + 50));
            }

            int randomNumber = (new Random()).nextInt(102);
            if (randomNumber >= 0 && randomNumber < 100) {
                this.robot.type(new int[]{40});
                continue;
            }
            switch ((new Random()).nextInt(106)) {
                case 100:
                    this.robot.type(new int[]{33});

                case 101:
                    this.robot.type(new int[]{145});
            }
        }
    }

    private void playWithPdf() throws Exception {
        Thread.sleep(2000L);

        int iterationNumber = 0;
        while (iterationNumber < 30) {
            iterationNumber++;
            sleep(100, 1900);

            if (this.random.nextInt(2) == 1) {
                this.robot.moveMouseToArbitraryLocation(100, 1000, 100, 1000);
                Thread.sleep((this.random.nextInt(200) + 30));
                this.robot.mousePressKey(1024);
            }

            int randomNumber = (new Random()).nextInt(46);
            if (randomNumber >= 0 && randomNumber < 36) {
                this.robot.typeRandomAlpahaNumericCharacter(new ArrayList<>());
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
            }
        }
    }

    private void playWithSQLDeveloper() throws Exception {
        Thread.sleep(2000L);

        int iterationNumber = 0;
        while (iterationNumber < 45) {
            iterationNumber++;
            sleep(100, 4900);

            if (this.random.nextInt(3) == 2) {
                this.robot.moveMouseToArbitraryLocation(100, 1000, 100, 1000);
                Thread.sleep((this.random.nextInt(500) + 50));
            }

            int randomNumber = (new Random()).nextInt(102);
            if (randomNumber >= 0 && randomNumber < 100) {
                this.robot.type(new int[]{40});
                continue;
            }
            switch ((new Random()).nextInt(106)) {
                case 100:
                    this.robot.type(new int[]{33});

                case 101:
                    this.robot.type(new int[]{145});
            }
        }
    }

    private void sleep(int min, int max) throws InterruptedException {
        Thread.sleep(Math.round(((this.random.nextInt(max) + min) / this.eventsRate)));
    }
}
