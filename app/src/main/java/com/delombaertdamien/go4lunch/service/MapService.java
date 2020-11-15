package com.delombaertdamien.go4lunch.service;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public interface MapService {

    void addMarker(GoogleMap googleMap, LatLng position, String nameOfPlace, Context context, boolean isBusy);
    void zoomInOnAPlace(GoogleMap googleMap, LatLng position, int v);
}
