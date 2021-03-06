package com.delombaertdamien.go4lunch.service;

import android.content.Context;

import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.utils.BitmapMarker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class MapServiceHelper implements MapService {

    @Override
    public void addMarker(GoogleMap googleMap, LatLng position, String nameOfPlace, Context context, boolean isBusy) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.title(nameOfPlace);
        if(isBusy) {
            markerOptions.icon(BitmapMarker.getBitmapDescriptorFromResourceDrawable(context, R.drawable.ic_baseline_restaurant_24, R.drawable.ic_location_ping_blue));
        }else{
            markerOptions.icon(BitmapMarker.getBitmapDescriptorFromResourceDrawable(context, R.drawable.ic_baseline_restaurant_24, R.drawable.ic_location_ping_orange));
        }
        googleMap.addMarker(markerOptions);
    }
    @Override
    public void zoomInOnAPlace(GoogleMap googleMap,LatLng position, int v) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, v));
    }
}
