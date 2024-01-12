package trafficLigth;

import enums.TrafficLightState;
import enums.TrafficLightType;
import message.StateMessage;
import visitor.CrossroadVisitor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TrafficLight {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    protected int trafficLightId;
    protected TrafficLightState state;
    protected int signalDurationTime = 5000;
    protected int visitorPassFrequency = 1000; //1 visitor can be permitted to go per 1 second
    protected CyclicBarrier startLettingVisitorsPass = new CyclicBarrier(1);
    protected AtomicBoolean isTurningOnOperationFinished = new AtomicBoolean(true);
    private TrafficLightType trafficLightType;
    private Queue<StateMessage> receivedEvents = new LinkedBlockingQueue<>();
    private Queue<CrossroadVisitor> waitingCrossroadVisitors = new LinkedBlockingQueue<>();

    public TrafficLight(int trafficLightId, TrafficLightType trafficLightType, TrafficLightState state) {
        this.trafficLightId = trafficLightId;
        this.trafficLightType = trafficLightType;
        this.state = state;
    }

    public TrafficLightType getTrafficLightType() {
        return trafficLightType;
    }

    public int getTrafficLightId() {
        return trafficLightId;
    }

    public int getWaitingVisitorsLength() {
        return waitingCrossroadVisitors.size();
    }

    public AtomicBoolean isTurningOnOperationFinished() {
        return isTurningOnOperationFinished;
    }

    public abstract void turnOnTheTrafficLight();

    public void letVisitorsPass() {
        try {
            startLettingVisitorsPass.await();
            Thread.sleep(signalDurationTime);
            this.state = TrafficLightState.Red;
            System.out.println(ANSI_RED + Timestamp.from(Instant.now()) + ": Traffic light id = " + this.trafficLightId + ", type = " + this.getTrafficLightType() + " changed state on " + this.state + ANSI_RESET);
            startLettingVisitorsPass.reset();
        } catch (InterruptedException e) {
            throw new RuntimeException(e + "\n" + Thread.currentThread().getName() + " were interrupted while sleeping for " + signalDurationTime + " ms");
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e + "\nException occurred using BarrierCycle = " + startLettingVisitorsPass + " in " + Thread.currentThread().getName());
        }
    }

    public void receiveEvent(StateMessage request) {
        System.out.println(Timestamp.from(Instant.now()) + ": Traffic light id = " + this.trafficLightId + ", type = " + this.getTrafficLightType() + " got a request to turn on for " + request.getSignalDurationTime() + " ms");
        receivedEvents.add(request);
    }

    public void newVisitor(CrossroadVisitor CrossroadVisitor) {
        waitingCrossroadVisitors.add(CrossroadVisitor);
        System.out.println(Timestamp.from(Instant.now()) + ": New visitor id = " + CrossroadVisitor.getId() + " has come to traffic light id = " + this.trafficLightId + ", type = " + this.getTrafficLightType());
    }

    public void startMessageQueueProcessing() {
        Runnable runnable = () -> {
            while (true) {
                if (receivedEvents.size() != 0) {
                    signalDurationTime = receivedEvents.poll().getSignalDurationTime();
                    turnOnTheTrafficLight();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        createLetVisitorsPassTask();
    }

    protected void createLetVisitorsPassTask() {
        TimerTask letTheVisitorPass = new TimerTask() {
            @Override
            public void run() {
                if (!waitingCrossroadVisitors.isEmpty() && state == TrafficLightState.Green) {
                    System.out.println(Timestamp.from(Instant.now()) + ": Visitor " + waitingCrossroadVisitors.poll().getId() + " has passed through the intersection");
                }
            }
        };

        //timer to let visitors pass through the intersection
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(letTheVisitorPass, 0, visitorPassFrequency);
        startLettingVisitorsPass = new CyclicBarrier(1, letTheVisitorPass);
    }
}
