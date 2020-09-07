package com.delombaertdamien.go4lunch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;


public class LogActivity extends AppCompatActivity {

    private ConstraintLayout mConstraintLayout;
    Button mButtonLogWithFacebook;
    Button mButtonLogWithGoogle;

    // Identifier Sign in Activity
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        setUI();
        setUp();
    }

    private void setUI() {
        mButtonLogWithFacebook = (Button) findViewById(R.id.log_with_facebook);
        mButtonLogWithGoogle = (Button) findViewById(R.id.log_with_google);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.activity_log_constraint_layout);
    }

    private void setUp() {
        mButtonLogWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LogActivity", "switch Activity with facebook");
                SignInActivityWithFacebook();
            }
        });
        mButtonLogWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LogActivity", "switch Activity with google");
                SignInActivityWithGoogle();

            }
        });
    }

    public void SignInActivityWithGoogle() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.mipmap.ic_launcher_logo)
                        .build(), RC_SIGN_IN);

    }

    public void SignInActivityWithFacebook() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.FacebookBuilder()
                        .build());

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.mipmap.ic_launcher_logo)
                        .build(), RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);

    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(mConstraintLayout, getString(R.string.connection_success), Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                if (response == null) {
                    Snackbar.make(mConstraintLayout, getString(R.string.connection_error_canceled), Snackbar.LENGTH_LONG).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(mConstraintLayout, getString(R.string.connection_error_no_internet), Snackbar.LENGTH_LONG).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Snackbar.make(mConstraintLayout, getString(R.string.connection_error_unknown), Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }
}