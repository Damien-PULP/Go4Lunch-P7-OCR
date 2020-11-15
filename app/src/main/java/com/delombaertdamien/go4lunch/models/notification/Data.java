package com.delombaertdamien.go4lunch.models.notification;


import java.io.Serializable;

public class Data implements Serializable {

    private String hello;

    public Data() {
        super();
    }

    public Data(String hello) {
        this.hello = hello;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    @Override
    public String toString() {
        return "Data{" +
                "hello='" + hello + '\'' +
                '}';
    }
}