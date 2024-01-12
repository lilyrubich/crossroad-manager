package trafficLigth;

import enums.MessageType;
import message.StateMessage;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

public class TrafficLightManager {

    private final Integer[][] combinationsOfTurnedOnLightIdsAtTheSameTime =  //combinations of turning on traffic lights types
            {
                    {0, 4},
                    {1, 5},
                    {2, 6},
                    {3, 7},
            };
    private final Map<Integer, Integer> trafficLightsCapacities = new HashMap<>();  //Ids of traffic lights and theirs actual capacity
    private final Map<Integer, Integer> trafficLightsOptimalTime = new HashMap<>();  //Ids of traffic lights and theirs optimal time to be turned on
    private final List<TrafficLight> trafficLightList = new ArrayList<>();
    private final int gettingActualStatesPeriod = 20000;
    private final int wholeCycleOfStatesPeriod = 20000; //combinationsOfTurnedOnLightIdsAtTheSameTime.length * signalDurationTime
    private final int signalDurationTime = 5000;

    public TrafficLightManager() {
    }

    public void addTrafficLight(TrafficLight trafficLight) {
        trafficLightList.add(trafficLight);
        trafficLightsCapacities.put(trafficLight.getTrafficLightId(), trafficLight.getWaitingVisitorsLength());
        trafficLightsOptimalTime.put(trafficLight.getTrafficLightId(), signalDurationTime);
    }

    public void startTrafficLightsChangeStatesCycle() {
        while (true) {
            for (Integer[] combination : combinationsOfTurnedOnLightIdsAtTheSameTime) {
                trafficLightList.stream()
                        .filter(x -> Arrays.asList(combination).contains(x.getTrafficLightType().getTrafficLightTypeId()))
                        .forEach(t -> {
                            if (trafficLightsOptimalTime.get(t.getTrafficLightId()) != 0) {
                                StateMessage request = new StateMessage(MessageType.TurningOnTheTrafficLightRequest,
                                        t.getTrafficLightId(), trafficLightsOptimalTime.get(t.getTrafficLightId()));
                                t.receiveEvent(request);
                            }
                        });

                trafficLightList.stream()
                        .filter(x -> Arrays.asList(combination).contains(x.getTrafficLightType().getTrafficLightTypeId()))
                        .forEach(t -> {
                            while (!t.isTurningOnOperationFinished().get())
                                Thread.yield();
                        });
            }
        }
    }

    public Map<Integer, Integer> getTrafficLightsCapacities() {
        for (TrafficLight t : trafficLightList) {
            trafficLightsCapacities.put(t.getTrafficLightId(), t.getWaitingVisitorsLength());
        }
        return trafficLightsCapacities;
    }

    public void startGettingActualStatesTask() {
        TimerTask getActualStatesTimerTask = new TimerTask() {
            @Override
            public void run() {
                getTrafficLightsCapacities();
                findTrafficLightsOptimalTime();
                System.out.println("---------------Optimal time of traffic lights to be on were recalculated---------------");
                for (TrafficLight t : trafficLightList) {
                    System.out.println(Timestamp.from(Instant.now()) + ": Current length of the queue in traffic light id " + t.getTrafficLightId() + " = " + t.getWaitingVisitorsLength() + ". New optimal time = " + trafficLightsOptimalTime.get(t.getTrafficLightId()));
                }
                System.out.println("--------------------------------------------------------------------------------------");
            }
        };

        Timer timer = new Timer(true); //daemon thread
        timer.scheduleAtFixedRate(getActualStatesTimerTask, 10000, gettingActualStatesPeriod);
    }

    protected void findTrafficLightsOptimalTime() {
        int commonCapacity = getCommonCapacity();

        for (Integer[] combination : combinationsOfTurnedOnLightIdsAtTheSameTime) {

            //common load of the combination
            int averageCombinationLoad = (int) (trafficLightList.stream()
                    .filter(x -> Arrays.asList(combination).contains(x.getTrafficLightType().getTrafficLightTypeId()))
                    .mapToInt(TrafficLight::getWaitingVisitorsLength)
                    .sum());

            trafficLightList.stream()
                    .filter(x -> Arrays.asList(combination).contains(x.getTrafficLightType().getTrafficLightTypeId()))
                    .forEach((t) -> {
                        trafficLightsOptimalTime.put(t.getTrafficLightId(),
                                wholeCycleOfStatesPeriod * averageCombinationLoad / commonCapacity);
                    });
        }
    }

    protected int getCommonCapacity() {
        int commonCapacity = 0;
        for (int capacity : trafficLightsCapacities.values()) {
            commonCapacity += capacity;
        }
        if (commonCapacity == 0) {
            return wholeCycleOfStatesPeriod;
        } else return commonCapacity; //== 0 ? wholeCycleOfStatesPeriod : commonCapacity;
    }
}
