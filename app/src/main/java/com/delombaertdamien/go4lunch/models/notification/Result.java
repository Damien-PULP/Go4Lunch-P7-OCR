package com.delombaertdamien.go4lunch.models.notification;

import java.io.Serializable;

public class Result implements Serializable {

    private String messageId;

    public Result() {
        super();
    }

    public Result(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}