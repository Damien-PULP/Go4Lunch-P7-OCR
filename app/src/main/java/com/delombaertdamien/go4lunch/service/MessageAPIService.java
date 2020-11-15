package com.delombaertdamien.go4lunch.service;

import com.delombaertdamien.go4lunch.models.notification.MyResponse;
import com.delombaertdamien.go4lunch.models.notification.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public interface MessageAPIService {
        @POST("fcm/send")
        Call<MyResponse> sendNotification (@Header("Authorization") String authorization, @Body Notification notification);
    }
