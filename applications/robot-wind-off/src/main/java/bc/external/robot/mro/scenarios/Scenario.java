package bc.external.robot.mro.scenarios;

public interface Scenario {

    void setStartDelaySeconds(Integer seconds);

    void setSessionDuration(Integer seconds);

    void windOff() throws Exception;
}
