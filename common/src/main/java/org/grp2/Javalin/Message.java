package org.grp2.Javalin;

public class Message {
    private int status;
    private String message;

    public Message(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public void set(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
