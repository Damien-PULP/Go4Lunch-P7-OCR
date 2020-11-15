package com.delombaertdamien.go4lunch.models.notification;

public class Token {

    private String token;

    public Token() {
    }
    public Token(String token) {
        this.token = token;
    }

    // --- GETTER --- //
    public String getToken() {
        return token;
    }
    // --- SETTER --- //
    public void setToken(String token) {
        this.token = token;
    }
}
