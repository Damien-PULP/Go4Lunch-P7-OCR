package com.delombaertdamien.go4lunch.ui.ListView;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delombaertdamien.go4lunch.ui.activity.MainActivity;
import com.delombaertdamien.go4lunch.ui.activity.MainViewModel;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.injections.InjectionMain;
import com.delombaertdamien.go4lunch.injections.MainViewModelFactory;
import com.delombaertdamien.go4lunch.models.POJO.Places.ResultsPlaces;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.ui.adapter.AdaptorListViewRestaurant;
import com.delombaertdamien.go4lunch.utils.FirestoreCall;
import com.delombaertdamien.go4lunch.utils.PlacesCall;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class ListViewFragment extends Fragment implements PlacesCall.CallbacksFetchNearbyPlace, FirestoreCall.CallbackFirestore {

    //UI
    private TextView mTextNoRestaurant;
    private ProgressDialog progressDialog;
    //ADAPTER
    private AdaptorListViewRestaurant mAdapterListView;
    //VIEW MODEL
    private MainViewModel viewModel;
    //MAPS USER, String = idPlace, Integer = nb user jointed
    private final Map<String, Integer> mapUserByPlaceID = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_view, container, false);

        configureToolbar(root);
        configureViewModel();
        initUI(root);
        getAllUsers();
        return root;
    }

    // configure information into toolbar
    private void configureToolbar (View root){
        MainActivity activity = ((MainActivity)root.getContext());
        activity.getSupportActionBar().setTitle(R.string.main_activity_title);
    }
    // Configuring the view model to using
    private void configureViewModel() {
        MainViewModelFactory mMainViewModelFactory = InjectionMain.provideViewModelFactory(getActivity());
        this.viewModel = new ViewModelProvider(this, mMainViewModelFactory).get(MainViewModel.class);
    }
    // Configure UI with information
    private void initUI(View root) {
        // SHOW LOADING DIALOG
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();

        RecyclerView mRecyclerViewRestaurant = root.findViewById(R.id.fragment_list_view_recycler_view);

        mAdapterListView = new AdaptorListViewRestaurant();
        mRecyclerViewRestaurant.setAdapter(mAdapterListView);
        mRecyclerViewRestaurant.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTextNoRestaurant = root.findViewById(R.id.fragment_list_view_no_restaurant_text);
        mTextNoRestaurant.setEnabled(true);
    }
    // Get all users to show number jointed a place
    private void getAllUsers(){
        FirestoreCall.getAllUsers(this);
        FirestoreCall.setUpdateDataRealTime(this);
    }

    // Get current location of and call Api Place with current location
    private void initPlaces (){
        Task<Location> locationTask = viewModel.getUserLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                callAPIPlaces(location.getLatitude() + ","+ location.getLongitude());
            }
        });
    }
    // Get All place proximity
    private void callAPIPlaces(String location){
        viewModel.getNearbyPlaces(this, location);
    }

    // Response of Method 'getAllUsers' - init mapUserByPlaceID with users.getLunchPlaceID && Call initPlace()
    @Override
    public void onSuccessGetUsers(List<Users> users) {
        mapUserByPlaceID.clear();
        for(Users user : users){
            final Integer val = mapUserByPlaceID.get(user.getLunchPlaceID());
            final Integer newVal = val == null ? 1 : val + 1 ;
            mapUserByPlaceID.put(user.getLunchPlaceID(), newVal);
        }
        initPlaces();
    }
    @Override
    public void onFailureGetUsers(Exception e) {

    }

    // Response of Method callApiPlace - get place && update adapter with place
    @Override
    public void onResponse(@Nullable ResultsPlaces places) {
        if(places != null){
            if(places.getResults().size() > 0) {
                mTextNoRestaurant.setEnabled(false);
            }else{
                mTextNoRestaurant.setEnabled(true);
            }
            mAdapterListView.updateData(places.getResults(), mapUserByPlaceID);
            // SHOW LOADING DIALOG
            progressDialog.dismiss();
        }
    }
    @Override
    public void onFailure(Throwable t) {
        Log.e("ListView", t.getMessage());
    }

}