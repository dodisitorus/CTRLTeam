package com.dodi.ctrlteam.model;

public class Message {
    String message;
    User sender;
    long createdAt;

    public Message(String message, User user, long createdAt) {
        this.message = message;
        this.sender = user;
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
