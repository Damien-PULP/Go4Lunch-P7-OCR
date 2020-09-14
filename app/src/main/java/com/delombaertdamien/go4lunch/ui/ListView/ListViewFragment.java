package com.delombaertdamien.go4lunch.ui.ListView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delombaertdamien.go4lunch.MainActivity;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.ui.AdaptorListViewRestaurant;

public class ListViewFragment extends Fragment {

    private RecyclerView mRecyclerViewRestaurant;
    private AdaptorListViewRestaurant mAdapterListView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_view, container, false);
        configureToolbar(root);
        configureRecyclerView(root);
        return root;
    }

    private void configureRecyclerView(View root) {
        mRecyclerViewRestaurant = root.findViewById(R.id.fragment_list_view_recycler_view);

        mAdapterListView = new AdaptorListViewRestaurant();
        mRecyclerViewRestaurant.setAdapter(mAdapterListView);
        mRecyclerViewRestaurant.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureToolbar (View root){
        MainActivity activity = ((MainActivity)root.getContext());
        activity.getSupportActionBar().setTitle(R.string.main_activity_title);
    }
}