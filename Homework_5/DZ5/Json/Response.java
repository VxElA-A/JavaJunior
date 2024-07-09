package org.example.DZ5.Json;

import java.util.Set;

public class Response {
    private String sender;
    private String message;
    private Set<String> clients;

    public Response() {}

    public Response(Set<String> clients) {
        this.clients = clients;
    }

    public Response(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }



    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<String> getClients() {
        return clients;
    }

    public void setClients(Set<String> clients) {
        this.clients = clients;
    }
}
