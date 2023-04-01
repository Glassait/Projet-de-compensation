package utils;

import java.io.Serializable;

public class Message implements Serializable {
    private final String messageBody;
    private final String address;

    public Message(String messageBody, String address) {
        this.messageBody = messageBody;
        this.address = address;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Message{messageBody='" + messageBody + "', address=" + address + "}";
    }
}
