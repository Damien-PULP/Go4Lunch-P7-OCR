package com.delombaertdamien.go4lunch.service;

import android.content.Intent;

import com.delombaertdamien.go4lunch.R;
import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class AuthenticationServiceHelper implements AuthenticationService {

    @Override
    public Intent getAuthUIOfSignWithFacebook() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.FacebookBuilder()
                        .build());

        return (AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.mipmap.ic_launcher_logo)
                        .build());
    }

    @Override
    public Intent getAuthUIOfSignWithGoogle() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


               return (AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.mipmap.ic_launcher_logo)
                        .build());
    }
}
