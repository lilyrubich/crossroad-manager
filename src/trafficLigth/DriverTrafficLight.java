package trafficLigth;

import enums.TrafficLightState;
import enums.TrafficLightType;

import java.sql.Timestamp;
import java.time.Instant;

public class DriverTrafficLight extends TrafficLight {

    private int yellowSignalDurationTime = 1000;

    public DriverTrafficLight(int trafficLightId, TrafficLightType trafficLightType, TrafficLightState state) {
        super(trafficLightId, trafficLightType, state);
    }

    public void turnOnTheTrafficLight() {
        isTurningOnOperationFinished.set(false);
        this.state = TrafficLightState.Yellow;
        System.out.println(ANSI_YELLOW + Timestamp.from(Instant.now()) + ": " + this.trafficLightId + ", type = " + this.getTrafficLightType() + " changed state on " + this.state + ANSI_RESET);
        try {
            Thread.sleep(yellowSignalDurationTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e + "\n" + Thread.currentThread().getName() + " were interrupted while sleeping for " + signalDurationTime + " ms");
        }
        this.state = TrafficLightState.Green;
        System.out.println(ANSI_GREEN + Timestamp.from(Instant.now()) + ": " + this.trafficLightId + ", type = " + this.getTrafficLightType() + " changed state on " + this.state + ANSI_RESET);
        letVisitorsPass();
        isTurningOnOperationFinished.set(true);
    }
}
