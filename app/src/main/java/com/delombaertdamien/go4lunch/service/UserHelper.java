package com.delombaertdamien.go4lunch.service;

import com.delombaertdamien.go4lunch.models.Users;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    public static CollectionReference getUsersCollection(){return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);}

    //CREATE
    public static Task<Void> createUser(String uid, String username, String urlPicture){
        Users user = new Users(uid, username, urlPicture, null, null);
        return UserHelper.getUsersCollection().document(uid).set(user);
    }
    //GET
    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }
    //UPDATE
    public static Task<Void> UpdateLunchPlace(String uid,String placeId, Date timestampLunch){
        return UserHelper.getUsersCollection().document(uid).update("lunchPlaceID", placeId, "timeStampLunchPlace", timestampLunch);
    }
    //DELETE
    public static Task<Void> deleteUser(String uid){return UserHelper.getUsersCollection().document(uid).delete(); }

}
