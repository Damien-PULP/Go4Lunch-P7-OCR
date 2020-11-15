package com.delombaertdamien.go4lunch.service;

import com.delombaertdamien.go4lunch.models.POJO.autocompleteByPlace.ResultAutoCompletePlace;
import com.delombaertdamien.go4lunch.models.POJO.Places.ResultDetails;
import com.delombaertdamien.go4lunch.models.POJO.Places.ResultsPlaces;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public interface PlacesService {

    @GET("json")
    Call<ResultsPlaces> getPlaces(@Query("location") String location, @Query("type") String type, @Query("radius") String radius, @Query("key") String API_KEY);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("json")
    Call<ResultDetails> getAPlace(@Query("place_id") String placeID, @Query("key") String API_KEY);

    Retrofit retrofitGetAPlace = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/details/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("json")
    Call<ResultAutoCompletePlace> getValueAutoCompleteByRequest(@Query("input") String input, @Query("type") String type, @Query("key") String API_KEY);

    Retrofit retrofitGetValueAutoCompletePlace = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/autocomplete/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
