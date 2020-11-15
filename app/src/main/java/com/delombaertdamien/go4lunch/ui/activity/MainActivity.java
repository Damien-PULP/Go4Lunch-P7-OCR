package com.delombaertdamien.go4lunch.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.injections.InjectionMain;
import com.delombaertdamien.go4lunch.injections.MainViewModelFactory;
import com.delombaertdamien.go4lunch.models.POJO.autocompleteByPlace.ResultAutoCompletePlace;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.service.MessageAPIService;
import com.delombaertdamien.go4lunch.ui.adapter.AdaptorListViewSuggestions;
import com.delombaertdamien.go4lunch.utils.ConfigureAlarmNotify;
import com.delombaertdamien.go4lunch.utils.FirestoreCall;
import com.delombaertdamien.go4lunch.utils.PlacesCall;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FirestoreCall.CallbackFirestoreUser, PlacesCall.GetAllPredictionOfSearchPlace {

    /** ----UI ---*/
    private RecyclerView recyclerViewSuggestion;
    //NAV HEADER
    private DrawerLayout drawerLayout;
    private ImageView iconProfileUser;
    private TextView emailProfileUser;
    private TextView nameProfileUser;
    /** -----------*/
    //VIEW MODEL
    private MainViewModel viewModel;
    private AdaptorListViewSuggestions adapterSuggestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureViewModel();
        configureBottomNavigationBar();
        configureUI();
        configureNavigationView();
        configureNotificationSend();
    }

    // Configuring the view model to using
    private void configureViewModel() {
        MainViewModelFactory mMainViewModelFactory = InjectionMain.provideViewModelFactory(this);
        this.viewModel = new ViewModelProvider(this, mMainViewModelFactory).get(MainViewModel.class);
    }
    // Configuring the bottom navigation bar and this controller to nav into Fragment
    private void configureBottomNavigationBar (){
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map_view, R.id.navigation_list_view, R.id.navigation_workmates)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }
    // Setup UI
    private void configureUI() {
        // --- Toolbar --- //
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        // --- Drawer Layout --- //
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // --- Suggestion --- //
        recyclerViewSuggestion = findViewById(R.id.activity_main_toolbar_recycler_view_suggestion);
        recyclerViewSuggestion.setLayoutManager(new LinearLayoutManager(this));
        adapterSuggestion = new AdaptorListViewSuggestions();
        recyclerViewSuggestion.setAdapter(adapterSuggestion);
    }
    // Setup UI information to User into Navigation view
    private void configureNavigationView (){
        NavigationView navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        ConstraintLayout header = (ConstraintLayout) navigationView.getHeaderView(0);
        this.iconProfileUser = (ImageView) header.findViewById(R.id.nav_icon_user);
        this.nameProfileUser = (TextView) header.findViewById(R.id.nav_name_user);
        this.emailProfileUser = (TextView) header.findViewById(R.id.nav_email_user);
        navigationView.setNavigationItemSelectedListener(this);

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

    // Send request of get Suggestion Place
    private void searchPredictionQueryTextChange (String value){
        PlacesCall.getAllPredictionOfSearchPlace(this, value);
    }

    // Configure the sending notification with AlarmManager
    private void configureNotificationSend() {
        ConfigureAlarmNotify.configureAlarmManager(this);
    }

    // Get current user of Firebase
    public FirebaseUser getCurrentUser (){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.activity_main_drawer_your_lunch:
                // detail restaurant chose
                FirestoreCall.getCurrentUser(this);
                break;
            case R.id.activity_main_drawer_favorite_activity:
                Intent intentFavorite = new Intent(this, FavoriteActivity.class);
                startActivity(intentFavorite);
                break;
            case R.id.activity_main_drawer_messages_activity:
                Intent intentChat = new Intent(this, ChatActivity.class);
                startActivity(intentChat);
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

        MenuItem search = menu.findItem(R.id.activity_main_search);
        SearchView searchView = (SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchPredictionQueryTextChange(s);
                return false;
            }
        });
        return true;
    }

    // Response to get object current user - call by onNavigationItemSelected, selected start DetailActivity with placeID of User in extras
    @Override
    public void onSuccessGetCurrentUser(Users user) {
        if(user.getLunchPlaceID() != null) {
            Intent intentYourLunch = new Intent(this, DetailsActivity.class);
            intentYourLunch.putExtra("placeID", user.getLunchPlaceID());
            startActivity(intentYourLunch);
        }else{
            Toast.makeText(this, R.string.alert_not_again_choose, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onFailureGetCurrentUser() {

    }

    // Response to get prediction of input search in toolbar - call by onCreateOptionsMenu, change input in searchView
    @Override
    public void onResponseGetAllPredictionsOfSearchPlace(ResultAutoCompletePlace result, String input) {
            adapterSuggestion.updateSuggestions(result.getPredictions(), input);
    }
    @Override
    public void onFailureGetAllPredictionsOfSearchPlace(Throwable t) {
        Log.e("MainActivity", t.getMessage());
    }

    // If back is pressed
    @Override
    public void onBackPressed() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

}