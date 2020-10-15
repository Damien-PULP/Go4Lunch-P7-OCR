package com.delombaertdamien.go4lunch.models;

import java.util.Date;

public class Message {

    private String message;
    private Date dateCreate;
    private String userID;

    public Message() {
    }

    public Message(String message, Date dateCreate, String userID) {
        this.message = message;
        this.dateCreate = dateCreate;
        this.userID = userID;
    }

    // --- GETTER --- //
    public String getMessage() {
        return message;
    }
    public Date getDateCreate() {
        return dateCreate;
    }
    public String getUserID() {
        return userID;
    }

    // --- SETTER --- //
    public void setMessage(String message) {
        this.message = message;
    }
    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

}
