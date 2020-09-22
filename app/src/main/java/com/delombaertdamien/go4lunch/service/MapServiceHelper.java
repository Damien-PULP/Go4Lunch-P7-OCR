package com.delombaertdamien.go4lunch.service;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapServiceHelper implements MapService {

    @Override
    public void addMarker(GoogleMap googleMap, LatLng position, String nameOfPlace) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.title(nameOfPlace);
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void zoomInOnAPlace(GoogleMap googleMap,LatLng position, int v) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, v));
    }
}
