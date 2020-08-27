package com.delombaertdamien.go4lunch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class LogActivity extends AppCompatActivity {

    Button mButtonLogWithFacebook;
    Button mButtonLogWithGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        setUI();
        setUp();
    }

    private void setUI (){
        mButtonLogWithFacebook = (Button) findViewById(R.id.log_with_facebook);
        mButtonLogWithGoogle = (Button) findViewById(R.id.log_with_google);
    }

    private void setUp (){
        mButtonLogWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LogActivity","switch Activity");
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                startActivity(intent);
            }
        });
        mButtonLogWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LogActivity","switch Activity");
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}