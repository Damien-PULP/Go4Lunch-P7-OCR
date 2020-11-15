package com.delombaertdamien.go4lunch.service;


import com.delombaertdamien.go4lunch.models.Favorite;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class FavoriteHelper {

    public static final String NAME_COLLECTION = "favorite";

    public static Task<QuerySnapshot> getAllPlaceIDFavorite (String idUser){
        return UserHelper.getUsersCollection()
                .document(idUser)
                .collection(NAME_COLLECTION).get();
    }
    public static Task<Void> addFavoritePlace (String idUser,String idPlace, String namePlace, String urlPlace){
        Favorite favorite = new Favorite(idPlace, namePlace, urlPlace);
        return UserHelper.getUsersCollection()
                .document(idUser)
                .collection(NAME_COLLECTION)
                .document(idPlace)
                .set(favorite, SetOptions.merge());
    }
    public static Task<Void> deleteFavoritePlace (String idUser, String idPlace){
        return  UserHelper.getUsersCollection()
                .document(idUser)
                .collection(NAME_COLLECTION)
                .document(idPlace)
                .delete();
    }
}
