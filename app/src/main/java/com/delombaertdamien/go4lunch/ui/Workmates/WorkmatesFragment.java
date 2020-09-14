package com.delombaertdamien.go4lunch.ui.Workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delombaertdamien.go4lunch.MainActivity;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.ui.AdaptorListViewRestaurant;
import com.delombaertdamien.go4lunch.ui.AdaptorListViewWorkmates;

public class WorkmatesFragment extends Fragment {

    private RecyclerView mRecyclerViewWorkmates;
    private AdaptorListViewWorkmates mAdapterListView;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_workmates, container, false);
        this.configureToolbar(root);
        this.configureRecyclerView(root);

        return root;
    }

    private void configureToolbar (View root){
        MainActivity activity = ((MainActivity)root.getContext());
        activity.getSupportActionBar().setTitle(R.string.main_activity_title_workmates);
    }

    private void configureRecyclerView(View root) {
        mRecyclerViewWorkmates = root.findViewById(R.id.fragment_workmates_recycler_view);

        mAdapterListView = new AdaptorListViewWorkmates();
        mRecyclerViewWorkmates.setAdapter(mAdapterListView);
        mRecyclerViewWorkmates.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}