package com.delombaertdamien.go4lunch.service;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public interface MapService {

    public void addMarker (GoogleMap googleMap, LatLng position, String nameOfPlace, Context context, boolean isBusy);
    public void zoomInOnAPlace (GoogleMap googleMap,LatLng position, int v);
}
