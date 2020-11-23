package com.delombaertdamien.go4lunch.models.notification;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Notification implements Serializable {


    private String to;
    private Data data;

    public Notification() {
        super();
    }

    public Notification(String to, Data data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @NotNull
    @Override
    public String toString() {
        return "Notification{" +
                "to='" + to + '\'' +
                ", data=" + data +
                '}';
    }
}