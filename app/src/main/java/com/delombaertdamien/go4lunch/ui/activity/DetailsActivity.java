package com.delombaertdamien.go4lunch.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.models.Favorite;
import com.delombaertdamien.go4lunch.models.POJO.Places.Result;
import com.delombaertdamien.go4lunch.models.POJO.Places.ResultDetails;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.service.FavoriteHelper;
import com.delombaertdamien.go4lunch.service.UserHelper;
import com.delombaertdamien.go4lunch.ui.adapter.AdaptorListViewWorkmates;
import com.delombaertdamien.go4lunch.utils.FirestoreCall;
import com.delombaertdamien.go4lunch.utils.PlacesCall;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class DetailsActivity extends AppCompatActivity implements PlacesCall.GetDetailOfPlaceCallbacks, View.OnClickListener, FirestoreCall.CallbackFirestore, FirestoreCall.CallbackFirestoreFavorite {

    /** ----UI ---*/
    ImageView mPhotoPlaceImage;
    TextView mNameOfPlaceText;
    TextView mInformationOfPlaceText;

    FloatingActionButton mFavoritesPlaceFab;

    LinearLayout mCallPlaceButton;
    LinearLayout mFavoritePlaceButton;
    LinearLayout mWebSitePlaceButton;

    ImageView mIconFavoriteButton;

    RecyclerView mRecyclerViewWorkmates;

    private ProgressDialog progressDialog;
    /** -----------*/
    AdaptorListViewWorkmates adapter;

    private String placeID;
    private Result place;

    private Boolean isCurrentUserEatHer = false;
    private Boolean isThisPlaceIsFavorite = false;

    private final SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        this.configureToolbar();
        this.configureUI();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                placeID = null;
            } else {
                placeID = extras.getString("placeID");
                getDetailsOfAPlace();
            }
        } else {
            placeID = (String) savedInstanceState.getSerializable("placeID");
            getDetailsOfAPlace();
        }
    }
    // Configure information into toolbar
    private void configureToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
    }
    // Setup UI
    private void configureUI() {
        //Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        //General detail place ini
        mFavoritesPlaceFab = (FloatingActionButton) findViewById(R.id.detail_activity_fab_favorites);
        mNameOfPlaceText = findViewById(R.id.details_activity_name_of_restaurant);
        mInformationOfPlaceText = findViewById(R.id.details_activity_information_of_restaurant);
        mCallPlaceButton = findViewById(R.id.detail_activity_call_place_button);
        mFavoritePlaceButton = findViewById(R.id.detail_activity_favorite_place_button);
        mIconFavoriteButton = findViewById(R.id.detail_activity_favorite_place_icon);
        mWebSitePlaceButton = findViewById(R.id.detail_activity_website_button);
        mPhotoPlaceImage = findViewById(R.id.details_activity_image_place);
        //Recycler View
        mRecyclerViewWorkmates = (RecyclerView) findViewById(R.id.detail_activity_workmates_of_place_recycler_view);
        adapter = new AdaptorListViewWorkmates(this, false);
        mRecyclerViewWorkmates.setAdapter(adapter);
        mRecyclerViewWorkmates.setLayoutManager(new LinearLayoutManager(this));
    }
    // Get detail of place with place id
    private void getDetailsOfAPlace() {
        PlacesCall.getDetailOfAPlace(this, placeID);
    }

    // Get all favorite to check if this place is a favorite
    private void getAllFavoriteOfThisUser() {
        FirestoreCall.getAllFavoritePlaceOfAUser(this, getCurrentUser().getUid());
    }

    // Update UI with information of this place - 'getAllUserOfThisPlace'
    private void updateUI() {
        //GENERAL
        mNameOfPlaceText.setText(place.getName());
        mInformationOfPlaceText.setText(place.getVicinity());
        //PHONE
        if (place.getInternationalPhoneNumber() != null) {
            mCallPlaceButton.setOnClickListener(this);
        } else {
            mCallPlaceButton.setEnabled(false);
        }
        // WEBSITE
        if (place.getWebsite() != null) {
            mWebSitePlaceButton.setOnClickListener(this);
        } else {
            mWebSitePlaceButton.setEnabled(false);
        }
        // FAVORITE
        if(isThisPlaceIsFavorite){
            mIconFavoriteButton.setImageResource(R.drawable.ic_baseline_star_24);
        }else{
            mIconFavoriteButton.setImageResource(R.drawable.ic_baseline_star_border_24);
        }
        mFavoritePlaceButton.setOnClickListener(this);

        mFavoritesPlaceFab.setColorFilter(Color.LTGRAY);
        mFavoritesPlaceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentUserEatHer) {
                    mFavoritesPlaceFab.setColorFilter(Color.LTGRAY);
                    UserHelper.updateLunchPlace(getCurrentUser().getUid(), null, null);
                    getAllUserOfThisPlace();
                    isCurrentUserEatHer = false;
                } else {
                    mFavoritesPlaceFab.setColorFilter(Color.GREEN);
                    Date date = new Date();
                    UserHelper.updateLunchPlace(getCurrentUser().getUid(), placeID, date);
                    getAllUserOfThisPlace();
                    Snackbar.make(view, getResources().getString(R.string.alert_msg_today_eat) + " " + place.getName(), Snackbar.LENGTH_LONG).show();
                    isCurrentUserEatHer = true;
                }
            }
        });
        //PHOTO
        if (place.getPhotos().size() > 0) {
            String baseUrlPhoto = "https://maps.googleapis.com/maps/api/place/photo";
            String widthPhoto = "?maxwidth=" + place.getPhotos().get(0).getWidth();
            String referencePhoto = "&photoreference=" + place.getPhotos().get(0).getPhotoReference();
            String url = baseUrlPhoto + widthPhoto + referencePhoto + "&key=" + "AIzaSyArhuiDxRj8manHc0BihLXgQ-E6qjJw6r4";

            Glide.with(this)
                    .load(url)
                    .apply(RequestOptions.centerCropTransform())
                    .into(mPhotoPlaceImage);
        }

        getAllUserOfThisPlace();
    }

    // Get all users jointed of this place
    private void getAllUserOfThisPlace() {
        FirestoreCall.getUsersOfAPlace(this, placeID);
    }

    // Check if this user eat in this place
    private void checkIfCurrentUserEatHer(List<Users> users) {
        for (Users user : users) {
            if (user.getLunchPlaceID() != null) {
                if (user.getLunchPlaceID().equals(placeID) && user.getUserId().equals(getCurrentUser().getUid())) {
                    isCurrentUserEatHer = true;
                    mFavoritesPlaceFab.setColorFilter(Color.GREEN);
                    break;
                }
            }
        }
    }

    // Response of Method 'getDetailOfAPlace' - call 'getAllFavoriteOfThisUser' to check if this place is a favorite
    @Override
    public void onResponseGetDetailOfPlace(ResultDetails result) {
        if (result.getResult() != null) {
            place = result.getResult();
            getAllFavoriteOfThisUser();
        } else {
            Log.d("DetailActivity", "nothing result");
        }
    }
    @Override
    public void onFailureGetDetailOfPlace(Throwable t) {
        Log.e("DetailActivity", t.getMessage());
    }

    // Response of Method 'getAllFavoriteOfThisUser' - call 'updateUI'
    @Override
    public void onSuccessGetAllFavoriteOfTheUser(List<Favorite> favorites) {
        for(Favorite favorite : favorites){
            if (favorite.getIdPlace().equals(placeID)) {
                isThisPlaceIsFavorite = true;
                break;
            }
        }
        updateUI();
        progressDialog.dismiss();
    }
    @Override
    public void onFailureGetAllFavoriteOfTheUser() {

    }

    // Response of Method 'getAllUserOfThisPlace' - update date adapter && call 'checkIfCurrentUserEatHer'
    @Override
    public void onSuccessGetUsers(List<Users> users) {
        adapter.updateData(users);
        checkIfCurrentUserEatHer(users);
    }
    @Override
    public void onFailureGetUsers(Exception e) {
        Log.e("DetailActivity", "Error firestore call " + e.getMessage());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_activity_call_place_button:
                Log.d("DetailActivity", "call phone number" + place.getInternationalPhoneNumber());
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + place.getInternationalPhoneNumber()));
                startActivity(intent);
                break;
            case R.id.detail_activity_favorite_place_button:
                if(!isThisPlaceIsFavorite) {
                    String url = null;
                    if (place.getPhotos().size() > 0) {
                        String baseUrlPhoto = "https://maps.googleapis.com/maps/api/place/photo";
                        String widthPhoto = "?maxwidth=" + place.getPhotos().get(0).getWidth();
                        String referencePhoto = "&photoreference=" + place.getPhotos().get(0).getPhotoReference();
                        url = baseUrlPhoto + widthPhoto + referencePhoto + "&key=" + "AIzaSyArhuiDxRj8manHc0BihLXgQ-E6qjJw6r4";
                    }
                    FavoriteHelper.addFavoritePlace(getCurrentUser().getUid(), place.getPlaceId(), place.getName(), url);
                    mIconFavoriteButton.setImageResource(R.drawable.ic_baseline_star_24);
                    isThisPlaceIsFavorite = true;
                }else{
                    FavoriteHelper.deleteFavoritePlace(getCurrentUser().getUid(), placeID);
                    mIconFavoriteButton.setImageResource(R.drawable.ic_baseline_star_border_24);
                    isThisPlaceIsFavorite = false;
                }
                break;
            case R.id.detail_activity_website_button:
                Log.d("DetailActivity", "run website " + place.getWebsite());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(place.getWebsite()));
                startActivity(browserIntent);
                break;
        }
    }

    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

}