package com.delombaertdamien.go4lunch;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;

import android.location.Location;

import com.delombaertdamien.go4lunch.models.POJO.OpeningHours;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.service.UserHelper;
import com.delombaertdamien.go4lunch.utils.PlacesCall;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainViewModel extends ViewModel {

    private static final int SIGN_OUT_TASK = 10;
    private static final int DELETE_USER_TASK = 20;

    FusedLocationProviderClient fusedLocationProviderClient;
    private WeakReference<Context> context;

    public MainViewModel(Context context) {
        this.context = new WeakReference<>(context);
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public void signOutUserFromFirebase(Context context) {
        AuthUI.getInstance()
                .signOut(context)
                .addOnSuccessListener((Activity) context, updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK, context));
    }

    public void deleteUserFromFirebase(Context context) {
        if (getCurrentUser() != null) {
            UserHelper.deleteUser(getCurrentUser().getUid());
            AuthUI.getInstance().delete(context).addOnSuccessListener((Activity) context, updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK, context));
        }
    }

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
    public void getNearbyPlaces(PlacesCall.Callbacks callbacks, String location) {
        PlacesCall.fetchNearbyPlaces(callbacks, location);
    }

    //GET LOCATION
    public Task<Location> getUserLocation() {
        if (ActivityCompat.checkSelfPermission(context.get(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context.get(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return fusedLocationProviderClient.getLastLocation();
    }


}
