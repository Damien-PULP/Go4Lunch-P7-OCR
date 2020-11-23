package com.delombaertdamien.go4lunch.ui.MapView;

import android.Manifest;
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

import com.delombaertdamien.go4lunch.BuildConfig;
import com.delombaertdamien.go4lunch.DI.DI;
import com.delombaertdamien.go4lunch.ui.activity.MainActivity;
import com.delombaertdamien.go4lunch.ui.activity.MainViewModel;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.injections.InjectionMain;
import com.delombaertdamien.go4lunch.injections.MainViewModelFactory;
import com.delombaertdamien.go4lunch.models.POJO.Places.Result;
import com.delombaertdamien.go4lunch.models.POJO.Places.ResultsPlaces;
import com.delombaertdamien.go4lunch.service.MapService;
import com.delombaertdamien.go4lunch.utils.PlacesCall;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class MapViewFragment extends Fragment implements PlacesCall.CallbacksFetchNearbyPlace {

    //MAPS
    private GoogleMap mMap;
    private static final int ACCESS_LOCATION_REQUEST_CODE = 1001;
    FusedLocationProviderClient fusedLocationProviderClient;
    private MapService mapService;
    //VIEW MODEL
    private MainViewModel viewModel;
    //PROGRESS DIALOG
    private ProgressDialog progressDialog;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map_view, container, false);

        configureToolbar(root);
        configureViewModel();
        init();
        initMaps();
        configureUI(root);
        return root;
    }

    // Configure information into toolbar
    private void configureToolbar(View root) {
        MainActivity activity = ((MainActivity) root.getContext());
        activity.getSupportActionBar().setTitle(R.string.main_activity_title);
    }
    // Configuring the view model to using
    private void configureViewModel() {
        MainViewModelFactory mMainViewModelFactory = InjectionMain.provideViewModelFactory(getActivity());
        this.viewModel = new ViewModelProvider(this, mMainViewModelFactory).get(MainViewModel.class);
    }
    // Init Place
    private void init() {
        mapService = DI.getMapApiService();
        // SHOW DIALOG LOADING
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        // Place init
        String API_KEY_PLACE = BuildConfig.ApiKey;
        Places.initialize(getActivity().getApplicationContext(), API_KEY_PLACE);
        PlacesClient placesClient = Places.createClient(getContext());
    }
    // Init Place and show map into fragment
    private void initMaps() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        SupportMapFragment mapsFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view_fragment);
        mapsFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;

                try {
                    boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style));
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
                    zoomToUserLocation();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), ACCESS_FINE_LOCATION)) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
                    }
                }
            }
        });

    }
    // Configure Fab
    private void configureUI(View root) {
        FloatingActionButton mFab = root.findViewById(R.id.fragment_map_view_fab_my_location);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomToUserLocation();
            }
        });
    }

    // REQUEST API WITH RETROFIT
    private void callAPI(String location) {
        viewModel.getNearbyPlaces(this, location);
    }
    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        if (statusCheck()) {
            Task<Location> locationTask = viewModel.getUserLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mapService.zoomInOnAPlace(mMap, latLng, 15);
                        callAPI(location.getLatitude() + "," + location.getLongitude());
                    }
                    //progressDialog.dismiss();
                }
            });
        } else {
            buildAlertMessageNoGps();
        }
    }

    // Response of CallApi - ResultPlace
    @Override
    public void onResponse(ResultsPlaces places) {
        if(places.getResults()!= null) {
            mMap.clear();
            List<Result> candidatesPlaces = places.getResults();
            for(int i = 0; i < candidatesPlaces.size(); i++){
                LatLng latLng = new LatLng(candidatesPlaces.get(i).getGeometry().getLocation().getLat(), candidatesPlaces.get(i).getGeometry().getLocation().getLng());
                mapService.addMarker(mMap,latLng, candidatesPlaces.get(i).getName(), getActivity(), true);
            }
        }
        progressDialog.dismiss();

    }
    @Override
    public void onFailure(Throwable t) {
        Log.e("MapsViewFragment", "fail bro" + t.getMessage());
    }

    //Check Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                zoomToUserLocation();
            } else {
                getActivity().finish();
            }
        }
    }
    public boolean statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // Show request enabled alert no GPS
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

}