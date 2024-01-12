package trafficLigth;

import enums.TrafficLightState;
import enums.TrafficLightType;

import java.sql.Timestamp;
import java.time.Instant;

public class PedestrianTrafficLight extends TrafficLight {

    public PedestrianTrafficLight(int trafficLightId, TrafficLightType trafficLightType, TrafficLightState state) {
        super(trafficLightId, trafficLightType, state);
    }

    @Override
    public void turnOnTheTrafficLight() {
        isTurningOnOperationFinished.set(false);
        this.state = TrafficLightState.Green;
        System.out.println(ANSI_GREEN + Timestamp.from(Instant.now()) + ": " + this.trafficLightId + ", type = " + this.getTrafficLightType() + " changed state on " + this.state + ANSI_RESET);
        letVisitorsPass();
        isTurningOnOperationFinished.set(true);
    }
}
