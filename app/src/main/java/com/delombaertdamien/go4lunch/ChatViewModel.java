package com.delombaertdamien.go4lunch;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.service.UserHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class ChatViewModel extends ViewModel {

    public interface ListenersStateRequestGetUser {
        void onSuccessGetUserSpeaker(Users user);
        void onSuccessGetUser(Users user);
        void onFailedGetUser(Exception e);
    }
    private String speakerUser;

    public String getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getSpeakerUser() {
        return speakerUser;
    }

    public void getObjectSpeakerUser (final ListenersStateRequestGetUser callback){
        UserHelper.getUser(speakerUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Users user = documentSnapshot.toObject(Users.class);
                callback.onSuccessGetUserSpeaker(user);
            }
        });
    }

    public void getObjectUser (final ListenersStateRequestGetUser callback, String userID){
        UserHelper.getUser(userID)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Users user = documentSnapshot.toObject(Users.class);
                callback.onSuccessGetUser(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailedGetUser(e);
            }
        });
    }
    public void setSpeakerUser(String speakerUser) {
        this.speakerUser = speakerUser;
    }
}
