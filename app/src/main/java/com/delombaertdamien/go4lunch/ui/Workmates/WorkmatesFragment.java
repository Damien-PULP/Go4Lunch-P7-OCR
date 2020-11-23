package com.delombaertdamien.go4lunch.ui.Workmates;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delombaertdamien.go4lunch.ui.activity.MainActivity;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.ui.adapter.AdaptorListViewWorkmates;
import com.delombaertdamien.go4lunch.utils.FirestoreCall;
import com.delombaertdamien.go4lunch.utils.MyAlarmReceiver;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class WorkmatesFragment extends Fragment implements FirestoreCall.CallbackFirestore, FirestoreCall.CallbackFirestoreUser {

    // ADAPTER
    private AdaptorListViewWorkmates mAdapterListView;
    // PROGRESS DIALOG
    ProgressDialog progressDialog;
    //JUST FOR PRESENTATION
    ExtendedFloatingActionButton eFab;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workmates, container, false);

        configureToolbar(root);
        initUI(root);
        getAllUsers();
        //JUST FOR PRESENTATION
        getObjCurrentUser();
        return root;
    }

    private void getObjCurrentUser() {
        FirestoreCall.getCurrentUser(this);
    }

    // Configure information into toolbar
    private void configureToolbar (View root){
        MainActivity activity = ((MainActivity)root.getContext());
        activity.getSupportActionBar().setTitle(R.string.main_activity_title_workmates);
    }
    // Configure UI with information
    private void initUI(View root) {
        // SHOW LOADING DIALOG
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        // Recycler view
        RecyclerView mRecyclerViewWorkmates = root.findViewById(R.id.fragment_workmates_recycler_view);
        //JUST FOR PRESENTATION
        eFab = root.findViewById(R.id.fragment_workmates_send_notification);

        mAdapterListView = new AdaptorListViewWorkmates(getActivity(), true);
        mRecyclerViewWorkmates.setAdapter(mAdapterListView);
        mRecyclerViewWorkmates.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    // Get all user of this application
    private void getAllUsers(){
        FirestoreCall.getAllUsers(this);
        FirestoreCall.setUpdateDataRealTime(this);
    }

    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // Response of Method 'getAllUsers' - List user
    @Override
    public void onSuccessGetUsers(List<Users> users) {
        String currentUserID = getCurrentUser().getUid();
        List<Users> workmatesList = new ArrayList<>();
        for(Users user : users){
            if(!user.getUserId().equals(currentUserID)){
                workmatesList.add(user);
            }
        }
        mAdapterListView.updateData(workmatesList);
        progressDialog.dismiss();
    }
    @Override
    public void onFailureGetUsers(Exception e) {
        Log.e("WorkmatesFragment", e.getMessage());
    }

    //JUST FOR TEST
    @Override
    public void onSuccessGetCurrentUser(final Users user) {
        eFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAlarmReceiver.sendNotification(user);
            }
        });
    }

    @Override
    public void onFailureGetCurrentUser() {

    }
}