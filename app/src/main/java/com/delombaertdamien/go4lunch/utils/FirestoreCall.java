package com.delombaertdamien.go4lunch.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.delombaertdamien.go4lunch.BuildConfig;
import com.delombaertdamien.go4lunch.models.Discussion;
import com.delombaertdamien.go4lunch.models.Favorite;
import com.delombaertdamien.go4lunch.models.POJO.Places.ResultDetails;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.service.ChatHelper;
import com.delombaertdamien.go4lunch.service.FavoriteHelper;
import com.delombaertdamien.go4lunch.service.PlacesService;
import com.delombaertdamien.go4lunch.service.UserHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class FirestoreCall {

    public static final String API_KEY = BuildConfig.ApiKey;

    /** --- CALLBACK --- */
    public interface CallbackFirestore {
        void onSuccessGetUsers(List<Users> users);
        void onFailureGetUsers(Exception e);
    }
    public interface CallbackFirestoreUser {
        void onSuccessGetCurrentUser(Users user);
        void onFailureGetCurrentUser();
    }
    public interface CallbackFirestoreUsersOfDiscussion {
        void onSuccessGetUsersOfTheDiscussion (Users uid1, Users uid2);
        void onFailureGetUsersOfTheDiscussion (Exception e);
    }
    public interface CallbackFirestoreDiscussion {
        void onSuccessGetAllDiscussions(List<Discussion> discussions);
        void onFailureGetAllDiscussions(Exception e);
    }
    public interface CallbackFirestoreFavorite {
        void onSuccessGetAllFavoriteOfTheUser(List<Favorite> favorites);
        void onFailureGetAllFavoriteOfTheUser();
    }
    public interface CallbackGetTokenAtCurrentUser {
        void onFailureGetCurrentToken(Exception e);
        void onSuccessGetCurrentToken(String token);
    }
    public interface CallbackGetAllInformationToConstructNotification {
        void onSuccessGetAllInformationToConstructNotification(String name, String nameRestaurant, List<Users> usersLunchByUser);
        void onFailureGetAllInformationToConstructNotification(Exception e);
    }

    /** --- METHOD --- */
    // Notification
    public static void getAllInformationToConstructNotification (final CallbackGetAllInformationToConstructNotification callback){
        UserHelper.getUser(FirebaseAuth.getInstance().getUid())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailureGetAllInformationToConstructNotification(e);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final Users user = documentSnapshot.toObject(Users.class);

                        if (user.getLunchPlaceID() != null) {
                            PlacesService service = PlacesService.retrofitGetAPlace.create(PlacesService.class);

                            Call<ResultDetails> call = service.getAPlace(user.getLunchPlaceID(), API_KEY);
                            call.enqueue(new Callback<ResultDetails>() {
                                @Override
                                public void onResponse(Call<ResultDetails> call, final Response<ResultDetails> response) {

                                    final ResultDetails detailPlace = response.body();

                                    UserHelper.getAllUsersByPlaceId(user.getLunchPlaceID())
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            })
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot querySnapshot) {
                                                    List<Users> users = new ArrayList<>();
                                                    for (QueryDocumentSnapshot querySnap : querySnapshot) {
                                                        Users user = querySnap.toObject(Users.class);
                                                        if(user.getLunchPlaceID() != null) {
                                                            users.add(user);
                                                        }
                                                    }
                                                    callback.onSuccessGetAllInformationToConstructNotification(user.getUsername(), detailPlace.getResult().getName(), users);
                                                }
                                            });

                                }

                                @Override
                                public void onFailure(Call<ResultDetails> call, Throwable t) {
                                    callback.onSuccessGetAllInformationToConstructNotification(user.getUsername(), null, null);
                                    Log.e("FirestoreCall", t.getMessage());
                                }
                            });
                        }else{
                            callback.onSuccessGetAllInformationToConstructNotification(user.getUsername(), null, null);
                        }
                    }
                });
    }
    // User
    public static void getTokenAtCurrentUser (final CallbackGetTokenAtCurrentUser callback){
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {

                        callback.onSuccessGetCurrentToken(s);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailureGetCurrentToken(e);
                    }
                });
    }
    public static void getAllUsers(final CallbackFirestore callback) {

        UserHelper.getAllUsers()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailureGetUsers(e);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<Users> users = new ArrayList<>();
                        for (QueryDocumentSnapshot querySnap : querySnapshot) {
                            Users user = querySnap.toObject(Users.class);
                            users.add(user);
                        }
                        callback.onSuccessGetUsers(users);
                    }
                });
    }
    public static void getUsersOfAPlace(final CallbackFirestore callback, final String placeID) {

        UserHelper.getAllUsersByPlaceId(placeID)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailureGetUsers(e);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<Users> users = new ArrayList<>();
                        for (QueryDocumentSnapshot querySnap : querySnapshot) {
                            Users user = querySnap.toObject(Users.class);
                            if(user.getLunchPlaceID() != null) {
                                users.add(user);
                            }
                        }
                        callback.onSuccessGetUsers(users);
                    }
                });
    }
    public static void getCurrentUser(final CallbackFirestoreUser callback) {
        UserHelper.getUser(FirebaseAuth.getInstance().getUid())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailureGetCurrentUser();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Users user = documentSnapshot.toObject(Users.class);
                        callback.onSuccessGetCurrentUser(user);
                    }
                });
    }
    public static void getUsersOfDiscussionByID (final CallbackFirestoreUsersOfDiscussion callback, final String uid1, final String uid2){
        UserHelper.getUsersOfADiscussionByID(uid1, uid2).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                List<Users> users = new ArrayList<>();
                for(QueryDocumentSnapshot querySnap : querySnapshot){
                    users.add(querySnap.toObject(Users.class));
                }
                callback.onSuccessGetUsersOfTheDiscussion(users.get(0), users.get(1));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailureGetUsersOfTheDiscussion(e);
            }
        });
    }
    public static void setUpdateDataRealTime(final CallbackFirestore callback) {
        UserHelper.getUsersCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    callback.onFailureGetUsers(e);
                } else if (querySnapshot != null & !querySnapshot.isEmpty()) {
                    List<Users> users = new ArrayList<>();
                    for (QueryDocumentSnapshot querySnap : querySnapshot) {
                        Users user = querySnap.toObject(Users.class);
                        users.add(user);
                    }
                    callback.onSuccessGetUsers(users);
                }
            }
        });
    }
    // FAVORITE
    public static void getAllFavoritePlaceOfAUser (final CallbackFirestoreFavorite callback, String userID){
        FavoriteHelper.getAllPlaceIDFavorite(userID)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<Favorite> favorites = new ArrayList<>();
                        for(QueryDocumentSnapshot snap : querySnapshot){
                            Favorite favorite = snap.toObject(Favorite.class);
                            favorites.add(favorite);
                        }
                        callback.onSuccessGetAllFavoriteOfTheUser(favorites);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailureGetAllFavoriteOfTheUser();
            }
        });
    }
    // DISCUSSION
    public static void getAllDiscussion(final CallbackFirestoreDiscussion callback) {
        ChatHelper.getAllDiscussions().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                List<Discussion> discussions = new ArrayList<>();
                Log.d("FirestoreCall", "document list" + querySnapshot.getDocuments().size());
                for (QueryDocumentSnapshot querySnap : querySnapshot) {
                    Log.d("FirestoreCall", "query" + querySnap.getId());
                    Discussion discussion = querySnap.toObject(Discussion.class);
                    discussions.add(discussion);
                }
                callback.onSuccessGetAllDiscussions(discussions);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailureGetAllDiscussions(e);
            }
        });
    }
}
