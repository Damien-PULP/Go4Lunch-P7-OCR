package com.delombaertdamien.go4lunch.models;

import androidx.annotation.Nullable;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class UsersWithoutPlaceId {

    //USER INFORMATION
    private String userId;
    private String username;
    @Nullable
    private String urlPicture;
    //TOKEN
    private String token;

    public UsersWithoutPlaceId (){

    }
    public UsersWithoutPlaceId(String userId, String username, @Nullable String urlPicture, String token) {
        this.userId = userId;
        this.username = username;
        this.urlPicture = urlPicture;
        this.token = token;
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
    public void setToken (String token){ this.token = token; }
}
