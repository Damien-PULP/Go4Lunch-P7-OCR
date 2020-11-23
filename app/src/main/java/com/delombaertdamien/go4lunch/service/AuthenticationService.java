package com.delombaertdamien.go4lunch.service;

/**
 * Create By Damien De Lombaert
 * 2020
 */
import android.content.Intent;

public interface AuthenticationService {

    Intent getAuthUIOfSignWithFacebook();
    Intent getAuthUIOfSignWithGoogle();
}
