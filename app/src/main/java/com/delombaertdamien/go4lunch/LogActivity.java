package com.delombaertdamien.go4lunch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.delombaertdamien.go4lunch.DI.DI;
import com.delombaertdamien.go4lunch.injections.Injection;
import com.delombaertdamien.go4lunch.injections.ViewModelFactory;
import com.delombaertdamien.go4lunch.service.AuthenticationService;
import com.delombaertdamien.go4lunch.service.UserHelper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


public class LogActivity extends AppCompatActivity {

    //UI
    private ConstraintLayout mConstraintLayout;
    Button mButtonLogWithFacebook;
    Button mButtonLogWithGoogle;

    private AuthenticationService authenticationService;
    // Identifier Sign in Activity
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        init();
        setUI();
        setUp();
    }

    private void init() {
        authenticationService = DI.getServiceAuthentication();
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
        startActivityForResult(
                authenticationService.getAuthUIOfSignWithGoogle(), RC_SIGN_IN);

    }
    public void SignInActivityWithFacebook() {
        startActivityForResult(
                authenticationService.getAuthUIOfSignWithFacebook(), RC_SIGN_IN);

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
                this.createUserInFirestore();
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

    private void createUserInFirestore (){

        if(this.getCurrentUser() != null){
            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();

            UserHelper.createUser(uid, username, urlPicture).addOnFailureListener(this.onFailureListener());
        }
    }

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.connection_error_unknown), Toast.LENGTH_LONG).show();
            }
        };
    }


    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

}