package com.delombaertdamien.go4lunch.ui.MapView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.delombaertdamien.go4lunch.DI.DI;
import com.delombaertdamien.go4lunch.MainActivity;
import com.delombaertdamien.go4lunch.MainViewModel;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.injections.InjectionMain;
import com.delombaertdamien.go4lunch.injections.MainViewModelFactory;
import com.delombaertdamien.go4lunch.models.POJO.Result;
import com.delombaertdamien.go4lunch.models.POJO.ResultsPlaces;
import com.delombaertdamien.go4lunch.service.MapService;
import com.delombaertdamien.go4lunch.utils.PlacesCall;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapViewFragment extends Fragment implements PlacesCall.Callbacks, GoogleMap.OnMarkerClickListener {

    //UI
    private FloatingActionButton mFab;

    private GoogleMap mMap;
    private  PlacesClient placesClient;

    private static final int ACCESS_LOCATION_REQUEST_CODE = 1001;
    FusedLocationProviderClient fusedLocationProviderClient;

    //API SERVICE
    private MapService mapService;
    //VIEW MODEL
    private MainViewModel viewModel;

    private ProgressDialog progressDialog;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map_view, container, false);
        configureToolbar(root);
        configureViewModel();
        init();
        initPlaces();
        initMaps();
        configureUI(root);
        return root;
    }

    private void init() {
        mapService = DI.getMapApiService();
        // SHOW DIALOG LOADING
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
    }
    private void initMaps() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        SupportMapFragment mapsFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view_fragment);
        mapsFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {

                mMap = googleMap;

                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getActivity(), R.raw.map_style));

                    if (!success) {
                        Log.e("MapFragment", "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e("MapFragment", "Can't find style. Error: ", e);
                }

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mapService.addMarker(mMap, latLng, "restaurant", getActivity(), true);
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

    private void configureViewModel() {
        MainViewModelFactory mMainViewModelFactory = InjectionMain.provideViewModelFactory(getActivity());
        this.viewModel = new ViewModelProvider(this, mMainViewModelFactory).get(MainViewModel.class);
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
            Task<Location> locationTask = viewModel.getUserLocation();
            mMap.setOnMarkerClickListener(this);
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mapService.zoomInOnAPlace(mMap, latLng, 15);
                       // callAPI(location.getLatitude() + ","+ location.getLongitude());

                    }
                    //TODO DELETE WHEN API IS ENABLED
                    progressDialog.dismiss();

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
        viewModel.getNearbyPlaces(this, location);
    }
    @Override
    public void onResponse(ResultsPlaces places) {
        if(places.getResults()!= null) {
            mMap.clear();
            List<Result> candidatesPlaces = places.getResults();
            for(int i = 0; i < candidatesPlaces.size(); i++){
                Log.d("MapsViewFragment", "Name of restaurant : " + candidatesPlaces.get(i).getName());
                LatLng latLng = new LatLng(candidatesPlaces.get(i).getGeometry().getLocation().getLat(), candidatesPlaces.get(i).getGeometry().getLocation().getLng());
                mapService.addMarker(mMap,latLng, candidatesPlaces.get(i).getName(), getActivity(), true);
            }
            // DISMISS DIALOG LOADING
            progressDialog.dismiss();
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("MapsViewFragment", "The restaurant is : " + marker.getTitle());

        return true;
    }
}