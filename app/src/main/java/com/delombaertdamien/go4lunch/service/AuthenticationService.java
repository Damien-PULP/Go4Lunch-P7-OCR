package com.delombaertdamien.go4lunch.service;

import android.content.Intent;

import com.firebase.ui.auth.AuthUI;

public interface AuthenticationService {

    public Intent getAuthUIOfSignWithFacebook ();
    public Intent getAuthUIOfSignWithGoogle ();
}
