package com.delombaertdamien.go4lunch.ui.MapView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.delombaertdamien.go4lunch.DI.DI;
import com.delombaertdamien.go4lunch.MainActivity;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.models.Places.Candidate;
import com.delombaertdamien.go4lunch.models.Places.Candidates;
import com.delombaertdamien.go4lunch.service.MapService;
import com.delombaertdamien.go4lunch.utils.PlacesCall;
import com.delombaertdamien.go4lunch.utils.PlacesService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static androidx.core.content.ContextCompat.getSystemService;

public class MapViewFragment extends Fragment implements  PlacesCall.Callbacks {

    //UI
    private FloatingActionButton mFab;

    private GoogleMap mMap;
    private  PlacesClient placesClient;

    private static final int ACCESS_LOCATION_REQUEST_CODE = 1001;
    FusedLocationProviderClient fusedLocationProviderClient;

    //API SERVICE
    private MapService mapService;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map_view, container, false);
        configureToolbar(root);
        init();
        initPlaces();
        initMaps();
        configureUI(root);
        return root;
    }

    private void init() {
        mapService = DI.getMapApiService();
    }
    private void initMaps() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        SupportMapFragment mapsFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view_fragment);
        mapsFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {

                mMap = googleMap;
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mapService.addMarker(mMap, latLng, "restaurant");
                    }
                });
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    enabledUserLocation();
                    zoomToUserLocation();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), ACCESS_FINE_LOCATION)) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
                    } else {

                    }
                }
            }
        });

    }
    private void initPlaces() {
        //init SDK
        // TODO - CHANGE LOCATION API KEY
        Places.initialize(getActivity().getApplicationContext(), String.valueOf(R.string.apiKeyGooglePlace));
        placesClient = Places.createClient(getContext());

    }


    private void configureToolbar(View root) {
        MainActivity activity = ((MainActivity) root.getContext());
        activity.getSupportActionBar().setTitle(R.string.main_activity_title);
    }
    private void configureUI(View root){
        mFab = root.findViewById(R.id.fragment_map_view_fab_my_location);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomToUserLocation();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enabledUserLocation();
                zoomToUserLocation();
            } else {
                Log.e("MapViewFragment", "error permission denied");
            }
        }
    }

    //TODO WARNING PERMISSION & ERROR SDK
    private void zoomToUserLocation() {

        if(statusCheck()) {
            @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mapService.zoomInOnAPlace(mMap, latLng, 15);
                    callAPI(location.getLatitude() + ","+ location.getLongitude());
                }
            });
        }else{
            buildAlertMessageNoGps();
        }
    }

    @SuppressLint("MissingPermission")
    private void enabledUserLocation() {

        mMap.setMyLocationEnabled(true);
    }

    public boolean statusCheck() {

        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.alert_message_no_gps)
                .setCancelable(false)
                .setPositiveButton(R.string.positive_answer, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.negative_answer, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    // REQUEST API WITH RETROFIT
    private void callAPI (String location){
        PlacesCall.fetchNearbyPlaces(this, location);
    }
    @Override
    public void onResponse(Candidates places) {
        if(places.getCandidates() != null) {
            List<Candidate> candidatesPlaces = places.getCandidates();
            Log.d("MapsViewFragment", "Status of candidates "+ places.getStatus());
            Log.d("MapsViewFragment", "The size of list is "+ candidatesPlaces.size());
        }else{
            Log.d("MapsViewFragment", "Status of candidates "+ places.getStatus());
            Log.d("MapsViewFragment", "The list of places is null");
        }

    }
    @Override
    public void onFailure() {
        Log.d("MapsViewFragment", "fail bro");
    }
}