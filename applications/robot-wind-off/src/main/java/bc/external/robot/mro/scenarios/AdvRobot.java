package bc.external.robot.mro.scenarios;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AdvRobot {

    private final Robot robot;
    private final Random random = new Random();

    public AdvRobot(Robot robot) {
        this.robot = robot;
    }

    void moveMouseToArbitraryLocation(int minX, int maxX, int minY, int maxY) {
        robot.mouseMove(
                generateArbitraryMouseXPosition(minX, maxX),
                generateArbitraryMouseYPosition(minY, maxY));
    }

    int generateArbitraryMouseXPosition(int minX, int maxX) {
//        Integer MAX_X = (Toolkit.getDefaultToolkit().getScreenSize()).width;
        return random.nextInt((maxX - minX) + 1) + minX;
    }

    int generateArbitraryMouseYPosition(int minY, int maxY) {
//        Integer MAX_Y = (Toolkit.getDefaultToolkit().getScreenSize()).width;
        return random.nextInt((maxY - minY) + 1) + minY;
    }

    void beep(int hz, int msec, double vol) throws LineUnavailableException {
        float SAMPLE_RATE = 8000f;

        byte[] buf = new byte[1];
        AudioFormat af
                = new AudioFormat(
                        SAMPLE_RATE, // sampleRate
                        8, // sampleSizeInBits
                        1, // channels
                        true, // signed
                        false);      // bigEndian
        try (SourceDataLine sdl = AudioSystem.getSourceDataLine(af)) {
            sdl.open(af);
            sdl.start();
            for (int i = 0; i < msec * 8; i++) {
                double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
                buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
                sdl.write(buf, 0, 1);
            }
            sdl.drain();
            sdl.stop();
        }
    }

    void type(int... keys) throws InterruptedException {
        for (int key : keys) {
            robot.keyPress(key);
            Thread.sleep((random.nextInt(60) + 10));
            robot.keyRelease(key);
            Thread.sleep((random.nextInt(60) + 10));
        }
    }

    void mousePressKey(int key) throws InterruptedException {
        robot.mousePress(key);
        Thread.sleep((random.nextInt(60) + 10));
        robot.mouseRelease(key);
        Thread.sleep((random.nextInt(60) + 10));
    }

    void hotkey(int... keys) throws InterruptedException {
        for (int key : keys) {
            robot.keyPress(key);
            Thread.sleep((random.nextInt(60) + 10));
        }

        for (int key : keys) {
            robot.keyRelease(key);
            Thread.sleep((random.nextInt(60) + 10));
        }
    }

    void typeRandomAlpahaNumericCharacter(List<Integer> ruleOuts) throws InterruptedException {
        switch (random.nextInt(36)) {
            case 0:
                if (!ruleOuts.contains(48)) {
                    type(new int[]{48});
                }
                break;

            case 1:
                if (!ruleOuts.contains(49)) {
                    type(new int[]{49});
                }
                break;

            case 2:
                if (!ruleOuts.contains(50)) {
                    type(new int[]{50});
                }
                break;

            case 3:
                if (!ruleOuts.contains(51)) {
                    type(new int[]{51});
                }
                break;

            case 4:
                if (!ruleOuts.contains(52)) {
                    type(new int[]{52});
                }
                break;

            case 5:
                if (!ruleOuts.contains(53)) {
                    type(new int[]{53});
                }
                break;

            case 6:
                if (!ruleOuts.contains(54)) {
                    type(new int[]{54});
                }
                break;

            case 7:
                if (!ruleOuts.contains(55)) {
                    type(new int[]{55});
                }
                break;

            case 8:
                if (!ruleOuts.contains(56)) {
                    type(new int[]{56});
                }
                break;

            case 9:
                if (!ruleOuts.contains(57)) {
                    type(new int[]{57});
                }
                break;

            case 10:
                if (!ruleOuts.contains(65)) {
                    type(new int[]{65});
                }
                break;

            case 11:
                if (!ruleOuts.contains(66)) {
                    type(new int[]{66});
                }
                break;

            case 12:
                if (!ruleOuts.contains(67)) {
                    type(new int[]{67});
                }
                break;

            case 13:
                if (!ruleOuts.contains(68)) {
                    type(new int[]{68});
                }
                break;

            case 14:
                if (!ruleOuts.contains(69)) {
                    type(new int[]{69});
                }
                break;

            case 15:
                if (!ruleOuts.contains(70)) {
                    type(new int[]{70});
                }
                break;

            case 16:
                if (!ruleOuts.contains(71)) {
                    type(new int[]{71});
                }
                break;

            case 17:
                if (!ruleOuts.contains(72)) {
                    type(new int[]{72});
                }
                break;

            case 18:
                if (!ruleOuts.contains(73)) {
                    type(new int[]{73});
                }
                break;

            case 19:
                if (!ruleOuts.contains(74)) {
                    type(new int[]{74});
                }
                break;

            case 20:
                if (!ruleOuts.contains(75)) {
                    type(new int[]{75});
                }
                break;

            case 21:
                if (!ruleOuts.contains(76)) {
                    type(new int[]{76});
                }
                break;

            case 22:
                if (!ruleOuts.contains(77)) {
                    type(new int[]{77});
                }
                break;

            case 23:
                if (!ruleOuts.contains(78)) {
                    type(new int[]{78});
                }
                break;

            case 24:
                if (!ruleOuts.contains(79)) {
                    type(new int[]{79});
                }
                break;

            case 25:
                if (!ruleOuts.contains(80)) {
                    type(new int[]{80});
                }
                break;

            case 26:
                if (!ruleOuts.contains(81)) {
                    type(new int[]{81});
                }
                break;

            case 27:
                if (!ruleOuts.contains(82)) {
                    type(new int[]{82});
                }
                break;

            case 28:
                if (!ruleOuts.contains(83)) {
                    type(new int[]{83});
                }
                break;

            case 29:
                if (!ruleOuts.contains(84)) {
                    type(new int[]{84});
                }
                break;

            case 30:
                if (!ruleOuts.contains(85)) {
                    type(new int[]{85});
                }
                break;

            case 31:
                if (!ruleOuts.contains(86)) {
                    type(new int[]{86});
                }
                break;

            case 32:
                if (!ruleOuts.contains(87)) {
                    type(new int[]{87});
                }
                break;

            case 33:
                if (!ruleOuts.contains(88)) {
                    type(new int[]{88});
                }
                break;

            case 34:
                if (!ruleOuts.contains(89)) {
                    type(new int[]{89});
                }
                break;

            case 35:
                if (!ruleOuts.contains(90)) {
                    type(new int[]{90});
                }
                break;
        }
    }

    void mouseWheel(int distance) {
        robot.mouseWheel(distance);
    }

    void mouseClickScrollWheelButton() throws InterruptedException {
        robot.mousePress(InputEvent.BUTTON2_MASK);
        Thread.sleep((new Random().nextInt(60) + 10));
        robot.mouseRelease(InputEvent.BUTTON2_MASK);
    }
}
