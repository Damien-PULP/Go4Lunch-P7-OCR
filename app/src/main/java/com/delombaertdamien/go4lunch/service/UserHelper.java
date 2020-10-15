package com.delombaertdamien.go4lunch.service;

import androidx.annotation.Nullable;

import com.delombaertdamien.go4lunch.models.Users;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.Date;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    public static CollectionReference getUsersCollection(){return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);}

    //CREATE
    public static Task<Void> createUser(String uid, String username, String urlPicture){
        Users user = new Users(uid, username, urlPicture, null, null);
        return UserHelper.getUsersCollection().document(uid).set(user, SetOptions.merge());
    }
    //GET
    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }
    //GET
    public static Task<QuerySnapshot> getUsersOfADiscussionByID (String uid1, String uid2){
        return UserHelper.getUsersCollection().whereIn("userId", Arrays.asList(uid1,  uid2)).get();
    }
    //GET
    public static Task<QuerySnapshot> getAllUsers(){
        return UserHelper.getUsersCollection().get();
    }

    //GET
    public static Task<QuerySnapshot> getAllUsersByPlaceId(String placeID){
        return UserHelper.getUsersCollection().whereEqualTo("lunchPlaceID", placeID).get();
    }
    //UPDATE
    public static Task<Void> updateLunchPlace(String uid,String placeId, String timestampLunch){
        return UserHelper.getUsersCollection().document(uid).update("lunchPlaceID", placeId, "dateLunchPlace", timestampLunch);
    }
    //DELETE
    public static Task<Void> deleteUser(String uid){return UserHelper.getUsersCollection().document(uid).delete(); }

}
