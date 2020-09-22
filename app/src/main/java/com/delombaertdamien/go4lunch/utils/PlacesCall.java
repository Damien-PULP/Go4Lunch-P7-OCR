package com.delombaertdamien.go4lunch.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import com.delombaertdamien.go4lunch.models.Places.Candidates;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlacesCall {

    // Callback for response or failure
    public interface Callbacks {
        void onResponse(@Nullable Candidates places);
        void onFailure();
    }

    // Call API and Get Nearby Place
    public static void fetchNearbyPlaces (Callbacks callbacks, String location){

        final WeakReference<Callbacks> cbRef = new WeakReference<Callbacks>(callbacks);

        PlacesService service = PlacesService.retrofit.create(PlacesService.class);

        Call<Candidates> call = service.getPlaces(location, "restaurant", "3000", "AIzaSyArhuiDxRj8manHc0BihLXgQ-E6qjJw6r4");

        call.enqueue(new Callback<Candidates>() {
            @Override
            public void onResponse(Call<Candidates> call, Response<Candidates> response) {
                if(cbRef.get() != null){
                    cbRef.get().onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<Candidates> call, Throwable t) {
                if (cbRef.get() != null){
                    cbRef.get().onFailure();
                }
            }
        });
    }


}
