package com.delombaertdamien.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.delombaertdamien.go4lunch.injections.Injection;
import com.delombaertdamien.go4lunch.injections.ViewModelFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //NAV HEADER
    private ImageView iconProfileUser;
    private TextView emailProfileUser;
    private TextView nameProfileUser;

    //VIEW MODEL
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureViewModel();
        configureBottomNavigationBar();
        configureUI();
        configureNavigationView();
        updateUIWhenCreating();
    }

    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.viewModel = new ViewModelProvider(this,mViewModelFactory).get(MainViewModel.class);
    }

    private void configureBottomNavigationBar (){
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map_view, R.id.navigation_list_view, R.id.navigation_workmates)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(navView, navController);
    }
    private void configureUI() {

        // --- Toolbar --- //
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        // --- Drawer Layout --- //
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void configureNavigationView (){
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        ConstraintLayout header = (ConstraintLayout) navigationView.getHeaderView(0);
        this.iconProfileUser = (ImageView) header.findViewById(R.id.nav_icon_user);
        this.nameProfileUser = (TextView) header.findViewById(R.id.nav_name_user);
        this.emailProfileUser = (TextView) header.findViewById(R.id.nav_email_user);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void updateUIWhenCreating(){

        if(viewModel.getCurrentUser() != null){

            if(viewModel.getCurrentUser().getPhotoUrl() != null){
                Glide.with(this)
                        .load(viewModel.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(iconProfileUser);
            }

            nameProfileUser.setText(viewModel.getCurrentUser().getDisplayName());
            emailProfileUser.setText(viewModel.getCurrentUser().getEmail());

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.activity_main_drawer_your_lunch:
                // detail restaurant chose
                break;
            case R.id.activity_main_drawer_settings:
                // switch settings
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_main_drawer_logout :
                // logout user
                this.viewModel.signOutUserFromFirebase(this);
                break;
            default:
                break;
        }

        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}