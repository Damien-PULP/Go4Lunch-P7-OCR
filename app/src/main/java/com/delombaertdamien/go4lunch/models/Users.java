package com.delombaertdamien.go4lunch.models;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Users {

    //USER INFORMATION
    private String userId;
    private String username;
    @Nullable private String urlPicture;
    //PLACE INFORMATION
    @Nullable private String lunchPlaceID;
    @Nullable private Date timeStampLunchPlace;

    public Users(String userId, String username, @Nullable String urlPicture, @Nullable String lunchPlaceID, @Nullable Date timeStampLunchPlace) {
        this.userId = userId;
        this.username = username;
        this.urlPicture = urlPicture;
        this.lunchPlaceID = lunchPlaceID;
        this.timeStampLunchPlace = timeStampLunchPlace;
    }

    //GETTER
    public String getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }
    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }
    @Nullable
    public String getLunchPlaceID() {
        return lunchPlaceID;
    }
    @ServerTimestamp
    @Nullable
    public Date getTimeStampLunchPlace (){ return timeStampLunchPlace; }
    //SETTER
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
    public void setLunchPlaceID(@Nullable String lunchPlaceID) {
        this.lunchPlaceID = lunchPlaceID;
    }
    public void setTimeStampLunchPlace(@Nullable Date timeStampLunchPlace){ this.timeStampLunchPlace = timeStampLunchPlace; }

}
