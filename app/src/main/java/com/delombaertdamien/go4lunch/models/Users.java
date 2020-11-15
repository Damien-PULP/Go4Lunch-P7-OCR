package com.delombaertdamien.go4lunch.models;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Nullable String dateLunchPlace;
    private final SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YY");

    public Users (){

    }
    public Users(String userId, String username, @Nullable String urlPicture, String token, @Nullable String lunchPlaceID, @Nullable String dateLunchPlace) {
        this.userId = userId;
        this.username = username;
        this.urlPicture = urlPicture;
        this.token = token;
        this.lunchPlaceID = lunchPlaceID;
        this.dateLunchPlace = dateLunchPlace;
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

        Calendar cal = getDateLunchPlace();

        if(cal != null){
            Calendar currentC = Calendar.getInstance();

            if(currentC.get(Calendar.DAY_OF_YEAR) != cal.get(Calendar.DAY_OF_YEAR)){
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
    public Calendar getDateLunchPlace(){
        if(dateLunchPlace != null) {
            Date date;
            try {
                date = formatDate.parse(dateLunchPlace);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }

            if(date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar;
            }else{
                return null;
            }

        }else{
            return null;
        }
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
    public void setDateLunchPlace(@Nullable String dateLunchPlace){ this.dateLunchPlace = dateLunchPlace; }
    public void setToken (String token){ this.token = token; }

}
