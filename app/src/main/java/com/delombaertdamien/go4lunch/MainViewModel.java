package com.delombaertdamien.go4lunch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.ref.WeakReference;


public class MainViewModel extends ViewModel {

    private static final int SIGN_OUT_TASK = 10;
    private static final int DELETE_USER_TASK = 20;


    public MainViewModel() {

    }

    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }
    public void signOutUserFromFirebase(Context context) {
        AuthUI.getInstance()
                .signOut(context)
                .addOnSuccessListener((Activity) context, updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK, context));
    }
    public void deleteUserFromFirebase(Context context) {
        if (getCurrentUser() != null) {
            AuthUI.getInstance().delete(context).addOnSuccessListener((Activity) context, updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK, context));
        }
    }



    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin, final Context context) {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin) {
                    case SIGN_OUT_TASK:
                        ((Activity) context).finish();
                        break;
                    case DELETE_USER_TASK:
                        Intent intent = new Intent(context, LogActivity.class);
                        ((Activity) context).startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        };
    }

}
