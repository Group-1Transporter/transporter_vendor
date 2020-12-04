package com.transportervendor.beans;

import java.io.Serializable;

public class Message implements Serializable {
    private String message,date,time,from,id,to;
    private long timestamp;

    public Message(String message, String date, String time, String from, String id, String to, long timestamp) {
        this.message = message;
        this.date = date;
        this.time = time;
        this.from = from;
        this.id = id;
        this.to = to;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Message() {
    }
}
