package com.delombaertdamien.go4lunch.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.injections.InjectionMain;
import com.delombaertdamien.go4lunch.injections.MainViewModelFactory;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class SettingsActivity extends AppCompatActivity {

    // UI ACCOUNT
    private ImageView iconUser;
    private TextView nameUser;
    private TextView emailUser;
    private Button deleteAccountButton;
    // VIEW MODEL
    private MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        configureViewModel();
        configureToolbar();
        configureUI();
        updateUIWhenIsCreating();
    }

    // Configuring the view model to using
    private void configureViewModel() {
        MainViewModelFactory factory = InjectionMain.provideViewModelFactory(this);
        viewModel = new ViewModelProvider(this,factory).get(MainViewModel.class);
    }
    // Configure Ui with information
    private void configureUI() {
        iconUser = findViewById(R.id.settings_activity_icon_user_account);
        nameUser = findViewById(R.id.settings_activity_name_user_account);
        emailUser = findViewById(R.id.settings_activity_email_user_account);
        deleteAccountButton = findViewById(R.id.settings_activity_delete_account);
    }
    // configure information into toolbar
    private void configureToolbar() {
        // --- Toolbar --- //
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_settings_toolbar);
        toolbar.setTitle(R.string.nav_menu_settings);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    // This is when all options is configured
    private void updateUIWhenIsCreating(){
        if(viewModel.getCurrentUser() != null){
            if(viewModel.getCurrentUser().getPhotoUrl() != null){
                Glide.with(this)
                        .load(viewModel.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(iconUser);
            }
            nameUser.setText(viewModel.getCurrentUser().getDisplayName());
            emailUser.setText(viewModel.getCurrentUser().getEmail());
        }

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage(R.string.pop_message_delete_account)
                        .setPositiveButton(R.string.positive_answer, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                viewModel.deleteUserFromFirebase(view.getContext());
                            }
                        })
                        .setNegativeButton(R.string.negative_answer, null)
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}