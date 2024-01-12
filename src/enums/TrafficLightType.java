package enums;

public enum TrafficLightType {

    trafficLightForTheDriverGoingWest(0),
    trafficLightForTheDriverGoingSouth(1),
    trafficLightForTheDriverGoingEast(2),
    trafficLightForTheDriverGoingNorth(3),

    trafficLightForThePedestrianOnTheSouthSide(4),
    trafficLightForThePedestrianOnTheWestSide(5),
    trafficLightForThePedestrianOnTheNorthSide(6),
    trafficLightForThePedestrianOnTheEastSide(7);

    private int trafficLightTypeId;

    TrafficLightType(int trafficLightTypeId) {
        this.trafficLightTypeId = trafficLightTypeId;
    }

    public int getTrafficLightTypeId() {
        return trafficLightTypeId;
    }
}
