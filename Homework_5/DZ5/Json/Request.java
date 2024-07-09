package org.example.DZ5.Json;

public class Request {
    private String type;
    private String sender;
    private String recipient;
    private String message;

    public Request() {}

    public Request(String type, String sender, String recipient, String message) {
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}