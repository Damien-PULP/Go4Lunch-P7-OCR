package com.delombaertdamien.go4lunch.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.delombaertdamien.go4lunch.models.POJO.Result;
import com.delombaertdamien.go4lunch.models.POJO.ResultDetails;
import com.delombaertdamien.go4lunch.models.POJO.ResultsPlaces;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlacesCall {

    // Callback for response or failure
    public interface Callbacks {
        void onResponse(@Nullable ResultsPlaces places);
        void onFailure();
    }

    public interface GetDetailOfPlaceCallbacks {
        void onResponseGetDetailOfPlace (ResultDetails result);
        void onFailureGetDetailOfPlace (Throwable t);
    }

    // Call API and Get Nearby Place
    public static void fetchNearbyPlaces (Callbacks callbacks, String location){

        final WeakReference<Callbacks> cbRef = new WeakReference<Callbacks>(callbacks);

        PlacesService service = PlacesService.retrofit.create(PlacesService.class);

        Call<ResultsPlaces> call = service.getPlaces(location, "restaurant", "1000", "AIzaSyArhuiDxRj8manHc0BihLXgQ-E6qjJw6r4");

        call.enqueue(new Callback<ResultsPlaces>() {
            @Override
            public void onResponse(Call<ResultsPlaces> call, Response<ResultsPlaces> response) {
                if(cbRef.get() != null){
                    cbRef.get().onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResultsPlaces> call, Throwable t) {
                if (cbRef.get() != null){
                    cbRef.get().onFailure();
                }
            }
        });
    }

    public static void getDetailOfAPlace (final GetDetailOfPlaceCallbacks callbacks, String placeID){

         PlacesService service = PlacesService.retrofitGetAPlace.create(PlacesService.class);

         Call<ResultDetails> call = service.getAPlace(placeID, "AIzaSyArhuiDxRj8manHc0BihLXgQ-E6qjJw6r4");
         call.enqueue(new Callback<ResultDetails>() {
             @Override
             public void onResponse(Call<ResultDetails> call, Response<ResultDetails> response) {
                 callbacks.onResponseGetDetailOfPlace(response.body());
             }

             @Override
             public void onFailure(Call<ResultDetails> call, Throwable t) {
                callbacks.onFailureGetDetailOfPlace(t);
             }
         });
    }

}
