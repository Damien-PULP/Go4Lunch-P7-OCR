package com.delombaertdamien.go4lunch.utils;

import androidx.annotation.Nullable;

import com.delombaertdamien.go4lunch.BuildConfig;
import com.delombaertdamien.go4lunch.models.POJO.Places.ResultDetails;
import com.delombaertdamien.go4lunch.models.POJO.Places.ResultsPlaces;

import com.delombaertdamien.go4lunch.models.POJO.autocompleteByPlace.ResultAutoCompletePlace;
import com.delombaertdamien.go4lunch.service.PlacesService;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class PlacesCall {

    public static final String API_KEY = BuildConfig.ApiKey;
    /** --- CALLBACK --- */
    public interface CallbacksFetchNearbyPlace {
        void onResponse(@Nullable ResultsPlaces places);
        void onFailure(Throwable t);
    }
    public interface GetDetailOfPlaceCallbacks {
        void onResponseGetDetailOfPlace (ResultDetails result);
        void onFailureGetDetailOfPlace (Throwable t);
    }
    public interface GetAllPredictionOfSearchPlace {
        void onResponseGetAllPredictionsOfSearchPlace (ResultAutoCompletePlace result, String input);
        void onFailureGetAllPredictionsOfSearchPlace(Throwable t);
    }
    /** --- METHOD --- */
    public static void fetchNearbyPlaces (CallbacksFetchNearbyPlace callbacksFetchNearbyPlace, String location){
        final WeakReference<CallbacksFetchNearbyPlace> cbRef = new WeakReference<CallbacksFetchNearbyPlace>(callbacksFetchNearbyPlace);

        PlacesService service = PlacesService.retrofit.create(PlacesService.class);
        Call<ResultsPlaces> call = service.getPlaces(location, "restaurant", "1000", API_KEY);

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
                    cbRef.get().onFailure(t);
                }
            }
        });
    }
    public static void getDetailOfAPlace (final GetDetailOfPlaceCallbacks callbacks, String placeID){
         PlacesService service = PlacesService.retrofitGetAPlace.create(PlacesService.class);

         Call<ResultDetails> call = service.getAPlace(placeID, API_KEY);
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
    public static void getAllPredictionOfSearchPlace (final GetAllPredictionOfSearchPlace callback, final String input){
        PlacesService service = PlacesService.retrofitGetValueAutoCompletePlace.create(PlacesService.class);
        Call<ResultAutoCompletePlace> call = service.getValueAutoCompleteByRequest(input, "establishment",API_KEY);
        call.enqueue(new Callback<ResultAutoCompletePlace>() {
            @Override
            public void onResponse(Call<ResultAutoCompletePlace> call, Response<ResultAutoCompletePlace> response) {
                callback.onResponseGetAllPredictionsOfSearchPlace(response.body(), input);
            }

            @Override
            public void onFailure(Call<ResultAutoCompletePlace> call, Throwable t) {
                callback.onFailureGetAllPredictionsOfSearchPlace(t);
            }
        });
    }
}
