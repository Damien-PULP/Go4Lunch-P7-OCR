package com.delombaertdamien.go4lunch.ui.activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.service.UserHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class ChatViewModel extends ViewModel {

    private String speakerUser;

    public interface ListenersStateRequestGetUser {
        void onSuccessGetUser(Users user);
    }

    // Get current user
    public String getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    // Get user speaker
    public String getSpeakerUser() {
        return speakerUser;
    }
    // get object a user
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

            }
        });
    }
    // set the speaker user
    public void setSpeakerUser(String speakerUser) {
        this.speakerUser = speakerUser;
    }
}
