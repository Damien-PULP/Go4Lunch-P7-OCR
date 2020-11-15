package com.delombaertdamien.go4lunch.models;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class Favorite {

    private String idPlace;
    private String namePlace;
    private String urlPlace;

    public Favorite() {
    }
    public Favorite(String idPlace, String namePlace, String urlPlace) {
        this.idPlace = idPlace;
        this.namePlace = namePlace;
        this.urlPlace = urlPlace;
    }

    // --- GETTER --- //
    public String getNamePlace() {
        return namePlace;
    }
    public String getIdPlace() {
        return idPlace;
    }
    public String getUrlPlace() {
        return urlPlace;
    }
    // --- SETTER --- //
    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }
    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
    }
    public void setUrlPlace(String urlPlace) {
        this.urlPlace = urlPlace;
    }

}
