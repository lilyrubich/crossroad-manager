package message;

import enums.MessageType;

public class StateMessage {

    private final MessageType messageType;
    private final int trafficLightSenderId;
    private final int signalDurationTime;

    public StateMessage(MessageType messageType, int trafficLightSenderId, int signalDurationTime) {
        this.messageType = messageType;
        this.trafficLightSenderId = trafficLightSenderId;
        this.signalDurationTime = signalDurationTime;
    }

    public int getSignalDurationTime() {
        return signalDurationTime;
    }
}
