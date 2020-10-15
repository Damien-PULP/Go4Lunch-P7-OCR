package com.delombaertdamien.go4lunch.utils;

import com.delombaertdamien.go4lunch.models.POJO.Result;
import com.delombaertdamien.go4lunch.models.POJO.ResultDetails;
import com.delombaertdamien.go4lunch.models.POJO.ResultsPlaces;
import com.delombaertdamien.go4lunch.models.Places.Candidates;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlacesService {

    @GET("json")
    Call<ResultsPlaces> getPlaces(@Query("location") String location, @Query("type") String type, @Query("radius") String radius, @Query("key") String API_KEY);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("json")
    Call<ResultDetails> getAPlace(@Query("place_id") String placeID, @Query("key") String API_KEY);

    public static final Retrofit retrofitGetAPlace = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/details/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
