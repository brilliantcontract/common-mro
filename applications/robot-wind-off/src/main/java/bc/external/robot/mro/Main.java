package bc.external.robot.mro;

import bc.external.robot.mro.scenarios.MROScenario;
import bc.external.robot.mro.scenarios.MCScenario;
import bc.external.robot.mro.scenarios.Scenario;

public class Main {

    public static void main(String[] args) throws Exception {
        Scenario scenario = null;

        if (args.length != 2) {
            help();
        }

        switch (args[0].toUpperCase()) {
            case "-MRO":
                scenario = new MROScenario();
                break;

            case "-MC":
                scenario = new MCScenario();
                break;

            default:
                help();
                break;
        }

        if (scenario == null) {
            System.out.println("You have to specify correct scenario.\n\n");
            help();
            System.exit(1);
        }

        try {
            Integer.parseInt(args[1]);
        } catch (NumberFormatException ignore) {
            System.out.println("You entered invalid session duration.\n\n");
            help();
            System.exit(1);
        }

        scenario.setSessionDuration(Integer.parseInt(args[1]) * 60);
        scenario.setStartDelaySeconds(10);
        scenario.windOff();
    }

    private static void help() {
        System.out.println(
                "HELP\n"
                + "Application have to receive 2 arguments, first is SCENARIO name and second is SESSION DURATION.\n"
                + "Example: java -jar robot.jar -mro 100\n"
                + "- this command will execute MRO scenario with 100 minutes duration.\n\n\n"
                + "Avaliable scenarios:\n"
                + "* -mro\tapply to MRO\n"
                + "* -mc\tapply to MC\n"
        );
    }

}
