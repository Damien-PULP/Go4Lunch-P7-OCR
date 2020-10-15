package com.delombaertdamien.go4lunch.ui.Workmates;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.ui.adapter.AdaptorListViewWorkmates;
import com.delombaertdamien.go4lunch.utils.FirestoreCall;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragment extends Fragment implements FirestoreCall.CallbackFirestore {

    private RecyclerView mRecyclerViewWorkmates;
    private AdaptorListViewWorkmates mAdapterListView;

    ProgressDialog progressDialog;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_workmates, container, false);
        this.configureToolbar(root);
        this.init();
        this.configureRecyclerView(root);
        this.getAllUsers();
        return root;
    }

    private void init() {
        // SHOW LOADING DIALOG
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
    }

    private void configureToolbar (View root){
        MainActivity activity = ((MainActivity)root.getContext());
        activity.getSupportActionBar().setTitle(R.string.main_activity_title_workmates);
    }

    private void configureRecyclerView(View root) {
        mRecyclerViewWorkmates = root.findViewById(R.id.fragment_workmates_recycler_view);

        mAdapterListView = new AdaptorListViewWorkmates(getActivity(), true);
        mRecyclerViewWorkmates.setAdapter(mAdapterListView);
        mRecyclerViewWorkmates.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getAllUsers(){
        FirestoreCall.getAllUsers(this);
        FirestoreCall.setUpdateDataRealTime(this);
    }

    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

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

    }
}