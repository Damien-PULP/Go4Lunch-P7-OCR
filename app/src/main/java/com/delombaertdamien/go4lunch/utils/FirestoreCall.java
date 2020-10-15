package com.delombaertdamien.go4lunch.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.delombaertdamien.go4lunch.models.Discussion;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.service.ChatHelper;
import com.delombaertdamien.go4lunch.service.UserHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreCall {

    public interface CallbackFirestore {
        void onSuccessGetUsers(List<Users> users);
        void onFailureGetUsers(Exception e);
    }
    public interface CallbackFirestoreUser {
        void onSuccessGetCurrentUser(Users user);
        void onFailureGetCurrentUser(Exception e);
    }
    public interface CallbackFirestoreUsersOfDiscussion {
        void onSuccessGetUsersOfTheDiscussion (Users uid1, Users uid2);
        void onFailureGetUsersOfTheDiscussion (Exception e);
    }
    public interface CallbackFirestoreDiscussion {
        void onSuccessGetAllDiscussions(List<Discussion> discussions);
        void onFailureGetAllDiscussions(Exception e);
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
                            users.add(user);
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
                        callback.onFailureGetCurrentUser(e);
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
