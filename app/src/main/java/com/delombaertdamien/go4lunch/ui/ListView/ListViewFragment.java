package com.delombaertdamien.go4lunch.ui.ListView;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
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

import com.delombaertdamien.go4lunch.MainActivity;
import com.delombaertdamien.go4lunch.MainViewModel;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.injections.InjectionMain;
import com.delombaertdamien.go4lunch.injections.MainViewModelFactory;
import com.delombaertdamien.go4lunch.models.POJO.ResultsPlaces;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.ui.adapter.AdaptorListViewRestaurant;
import com.delombaertdamien.go4lunch.utils.FirestoreCall;
import com.delombaertdamien.go4lunch.utils.PlacesCall;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewFragment extends Fragment implements PlacesCall.Callbacks, FirestoreCall.CallbackFirestore {

    private TextView mTextNoRestaurant;
    private RecyclerView mRecyclerViewRestaurant;
    private AdaptorListViewRestaurant mAdapterListView;
    private MainViewModel viewModel;
    private ProgressDialog progressDialog;

    private Map<String, Integer> mapUserByPlaceID = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_view, container, false);
        configureToolbar(root);
        configureViewModel();
        init();
        configureRecyclerView(root);
        getAllUsers();
        return root;
    }

    private void init() {
        // SHOW LOADING DIALOG
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
    }

    private void configureViewModel() {
        MainViewModelFactory mMainViewModelFactory = InjectionMain.provideViewModelFactory(getActivity());
        this.viewModel = new ViewModelProvider(this, mMainViewModelFactory).get(MainViewModel.class);
    }
    private void configureRecyclerView(View root) {
        mRecyclerViewRestaurant = root.findViewById(R.id.fragment_list_view_recycler_view);

        mAdapterListView = new AdaptorListViewRestaurant(getActivity());
        mRecyclerViewRestaurant.setAdapter(mAdapterListView);
        mRecyclerViewRestaurant.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTextNoRestaurant = root.findViewById(R.id.fragment_list_view_no_restaurant_text);
        mTextNoRestaurant.setEnabled(true);
    }

    private void configureToolbar (View root){
        MainActivity activity = ((MainActivity)root.getContext());
        activity.getSupportActionBar().setTitle(R.string.main_activity_title);
    }
    private void initPlaces (){
        Task<Location> locationTask = viewModel.getUserLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                callAPIPlaces(location.getLatitude() + ","+ location.getLongitude());
            }
        });
    }

    private void callAPIPlaces(String location){
        viewModel.getNearbyPlaces(this, location);
    }

    private void getAllUsers(){
        FirestoreCall.getAllUsers(this);
        FirestoreCall.setUpdateDataRealTime(this);
    }

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
    @Override
    public void onResponse(@Nullable ResultsPlaces places) {
        if(places != null){

            if(places.getResults().size() > 0) {
                mTextNoRestaurant.setEnabled(false);
                mAdapterListView.updateData(places.getResults(), mapUserByPlaceID);
            }else{
                mTextNoRestaurant.setEnabled(true);
                mAdapterListView.updateData(places.getResults(), mapUserByPlaceID);
            }
            // SHOW LOADING DIALOG
            progressDialog.dismiss();
        }
    }
    @Override
    public void onFailure() {

    }

}