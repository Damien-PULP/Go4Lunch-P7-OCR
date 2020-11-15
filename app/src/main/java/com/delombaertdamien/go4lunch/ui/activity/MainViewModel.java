package com.delombaertdamien.go4lunch.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;

import android.location.Location;

import com.delombaertdamien.go4lunch.service.UserHelper;
import com.delombaertdamien.go4lunch.utils.PlacesCall;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.ref.WeakReference;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class MainViewModel extends ViewModel {

    private static final int SIGN_OUT_TASK = 10;
    private static final int DELETE_USER_TASK = 20;

    final FusedLocationProviderClient fusedLocationProviderClient;
    private final WeakReference<Context> context;

    public MainViewModel(Context context) {
        this.context = new WeakReference<>(context);
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // GET CURRENT USER
    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    //SIGN OUT USER
    public void signOutUserFromFirebase(Context context) {
        AuthUI.getInstance()
                .signOut(context)
                .addOnSuccessListener((Activity) context, updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK, context));
    }
    // DELETE USER
    public void deleteUserFromFirebase(Context context) {
        if (getCurrentUser() != null) {
            UserHelper.deleteUser(getCurrentUser().getUid());
            AuthUI.getInstance().delete(context).addOnSuccessListener((Activity) context, updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK, context));
        }
    }
    // UPDATE UI
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin, final Context context) {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin) {
                    case SIGN_OUT_TASK:
                        ((Activity) context).finish();
                        break;
                    case DELETE_USER_TASK:
                        Intent intent = new Intent(context, LogActivity.class);
                        ((Activity) context).startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        };
    }
    //REQUEST API PLACES
    public void getNearbyPlaces(PlacesCall.CallbacksFetchNearbyPlace callbacksFetchNearbyPlace, String location) {
        PlacesCall.fetchNearbyPlaces(callbacksFetchNearbyPlace, location);
    }
    //GET LOCATION
    public Task<Location> getUserLocation() {
        if (ActivityCompat.checkSelfPermission(context.get(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context.get(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return fusedLocationProviderClient.getLastLocation();
    }


}
