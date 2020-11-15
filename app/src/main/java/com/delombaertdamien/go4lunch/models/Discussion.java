package com.delombaertdamien.go4lunch.models;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class Discussion {

    //ONLY DISCUSSION
    private String discussionID;
    private String lastMessage;
    //USER ONE
    private String userOneID;
    private String userOneName;
    private String userOneUrlIcon;
    //USER TWO
    private String userTwoID;
    private String userTwoName;
    private String userTwoUrlIcon;

    public Discussion() {
    }
    public Discussion(String discussionID, String nameUserOne, String userOneID, String userOneUrlIcon, String nameUserTwo, String userTwoID, String userTwoUrlIcon, String lastMessage) {

        this.discussionID = discussionID;
        this.lastMessage = lastMessage;

        this.userOneName = nameUserOne;
        this.userOneUrlIcon = userOneUrlIcon;
        this.userOneID = userOneID;

        this.userTwoName = nameUserTwo;
        this.userTwoID = userTwoID;
        this.userTwoUrlIcon = userTwoUrlIcon;
    }

    // --- GETTER --- //
    public String getDiscussionID() {
        return discussionID;
    }
    public String getLastMessage() {
        return lastMessage;
    }

    public String getNameUserOne() {
        return userOneName;
    }
    public String getUrlIconUserOne() {
        return userOneUrlIcon;
    }
    public String getIDUserOne() {
        return userOneID;
    }

    public String getNameUserTwo() {
        return userTwoName;
    }
    public String getUrlIconUserTwo() {
        return userTwoUrlIcon;
    }
    public String getIDUserTwo() {
        return userTwoID;
    }

    // --- SETTER --- //
    public void setDiscussionID(String discussionID) {
        this.discussionID = discussionID;
    }
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setNameUserOne(String name) {
        this.userOneName = name;
    }
    public void setUrlIconUserOne(String urlIcon) {
        this.userOneUrlIcon = urlIcon;
    }
    public void setIDUserOne(String userOneID) {
        this.userOneID = userOneID;
    }

    public void setNameUserTwo(String name) {
        this.userTwoName = name;
    }
    public void setUrlIconUserTwo(String urlIcon) {
        this.userTwoUrlIcon = urlIcon;
    }
    public void setIDUserTwo(String userOneID) {
        this.userTwoID = userOneID;
    }

}
