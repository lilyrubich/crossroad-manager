package main;

import enums.TrafficLightState;
import enums.TrafficLightType;
import trafficLigth.DriverTrafficLight;
import trafficLigth.PedestrianTrafficLight;
import trafficLigth.TrafficLight;
import trafficLigth.TrafficLightManager;
import visitor.CrossroadVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {


        TrafficLight trafficLightForTheDriverGoingWest = new DriverTrafficLight(0, TrafficLightType.trafficLightForTheDriverGoingWest, TrafficLightState.Red);
        TrafficLight trafficLightForTheDriverGoingSouth = new DriverTrafficLight(1, TrafficLightType.trafficLightForTheDriverGoingSouth, TrafficLightState.Red);
        TrafficLight trafficLightForTheDriverGoingEast = new DriverTrafficLight(2, TrafficLightType.trafficLightForTheDriverGoingEast, TrafficLightState.Red);
        TrafficLight trafficLightForTheDriverGoingNorth = new DriverTrafficLight(3, TrafficLightType.trafficLightForTheDriverGoingNorth, TrafficLightState.Red);

        TrafficLight trafficLightForThePedestrianOnTheSouthSide = new PedestrianTrafficLight(4, TrafficLightType.trafficLightForThePedestrianOnTheSouthSide, TrafficLightState.Red);
        TrafficLight trafficLightForThePedestrianOnTheWestSide = new PedestrianTrafficLight(5, TrafficLightType.trafficLightForThePedestrianOnTheWestSide, TrafficLightState.Red);
        TrafficLight trafficLightForThePedestrianOnTheNorthSide = new PedestrianTrafficLight(6, TrafficLightType.trafficLightForThePedestrianOnTheNorthSide, TrafficLightState.Red);
        TrafficLight trafficLightForThePedestrianOnTheEastSide = new PedestrianTrafficLight(7, TrafficLightType.trafficLightForThePedestrianOnTheEastSide, TrafficLightState.Red);
        TrafficLight trafficLightForThePedestrianOnTheSouthSide1 = new PedestrianTrafficLight(8, TrafficLightType.trafficLightForThePedestrianOnTheSouthSide, TrafficLightState.Red);
        TrafficLight trafficLightForThePedestrianOnTheWestSide1 = new PedestrianTrafficLight(9, TrafficLightType.trafficLightForThePedestrianOnTheWestSide, TrafficLightState.Red);
        TrafficLight trafficLightForThePedestrianOnTheNorthSide1 = new PedestrianTrafficLight(10, TrafficLightType.trafficLightForThePedestrianOnTheNorthSide, TrafficLightState.Red);
        TrafficLight trafficLightForThePedestrianOnTheEastSide1 = new PedestrianTrafficLight(11, TrafficLightType.trafficLightForThePedestrianOnTheEastSide, TrafficLightState.Red);


        List<TrafficLight> trafficLights = new ArrayList<>();
        trafficLights.add(trafficLightForTheDriverGoingWest);
        trafficLights.add(trafficLightForTheDriverGoingSouth);
        trafficLights.add(trafficLightForTheDriverGoingEast);
        trafficLights.add(trafficLightForTheDriverGoingNorth);

        trafficLights.add(trafficLightForThePedestrianOnTheSouthSide);
        trafficLights.add(trafficLightForThePedestrianOnTheWestSide);
        trafficLights.add(trafficLightForThePedestrianOnTheNorthSide);
        trafficLights.add(trafficLightForThePedestrianOnTheEastSide);
        trafficLights.add(trafficLightForThePedestrianOnTheSouthSide1);
        trafficLights.add(trafficLightForThePedestrianOnTheWestSide1);
        trafficLights.add(trafficLightForThePedestrianOnTheNorthSide1);
        trafficLights.add(trafficLightForThePedestrianOnTheEastSide1);


        for (int i = 0; i < trafficLights.size(); i++) {
            trafficLights.get(i).startMessageQueueProcessing();
        }

        TrafficLightManager trafficLightManager = new TrafficLightManager();
        trafficLightManager.addTrafficLight(trafficLightForTheDriverGoingWest);
        trafficLightManager.addTrafficLight(trafficLightForTheDriverGoingSouth);
        trafficLightManager.addTrafficLight(trafficLightForTheDriverGoingEast);
        trafficLightManager.addTrafficLight(trafficLightForTheDriverGoingNorth);

        trafficLightManager.addTrafficLight(trafficLightForThePedestrianOnTheSouthSide);
        trafficLightManager.addTrafficLight(trafficLightForThePedestrianOnTheWestSide);
        trafficLightManager.addTrafficLight(trafficLightForThePedestrianOnTheNorthSide);
        trafficLightManager.addTrafficLight(trafficLightForThePedestrianOnTheEastSide);
        trafficLightManager.addTrafficLight(trafficLightForThePedestrianOnTheSouthSide1);
        trafficLightManager.addTrafficLight(trafficLightForThePedestrianOnTheWestSide1);
        trafficLightManager.addTrafficLight(trafficLightForThePedestrianOnTheNorthSide1);
        trafficLightManager.addTrafficLight(trafficLightForThePedestrianOnTheEastSide1);


        TimerTask passengerTask = new TimerTask() {
            int visitorsCount = 0;

            @Override
            public void run() {
                CrossroadVisitor CrossroadVisitor = new CrossroadVisitor(visitorsCount++);
                int randomTrafficLightId = 0 + (int) (Math.random() * 11);
                trafficLights.get(randomTrafficLightId).newVisitor(CrossroadVisitor);
            }
        };

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(passengerTask, 500, 1000);


        trafficLightManager.startGettingActualStatesTask();
        trafficLightManager.startTrafficLightsChangeStatesCycle();

    }
}