package com.delombaertdamien.go4lunch.models;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Calendar;
import java.util.Date;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class Users {

    //USER INFORMATION
    private String userId;
    private String username;
    @Nullable private String urlPicture;
    //TOKEN
    private String token;
    //PLACE INFORMATION

    @Nullable private String lunchPlaceID;
    @ServerTimestamp @Nullable Date dateLunchPlaceChoice;

    public Users (){

    }
    public Users(String userId, String username, @Nullable String urlPicture, String token, @Nullable String lunchPlaceID, @Nullable Date dateLunchPlaceChoice) {
        this.userId = userId;
        this.username = username;
        this.urlPicture = urlPicture;
        this.token = token;
        this.lunchPlaceID = lunchPlaceID;
        this.dateLunchPlaceChoice = dateLunchPlaceChoice;
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

        if(dateLunchPlaceChoice != null){

            Calendar currentCal= Calendar.getInstance();
            Calendar calLunchPlace = Calendar.getInstance();
            calLunchPlace.setTime(dateLunchPlaceChoice);

            if(currentCal.get(Calendar.DAY_OF_MONTH) == calLunchPlace.get(Calendar.DAY_OF_MONTH)){
                return lunchPlaceID;
            }else{
                return null;
            }
        }else{
            return null;
        }
        //return lunchPlaceID;

    }
    @Nullable
    public Date getDateLunchPlace(){
        return dateLunchPlaceChoice;
    }
    public String getToken (){ return token; }
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
    public void setDateLunchPlace(@Nullable Date dateLunchPlaceChoice){ this.dateLunchPlaceChoice = dateLunchPlaceChoice; }
    public void setToken (String token){ this.token = token; }

}
