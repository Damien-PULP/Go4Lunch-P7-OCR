package com.delombaertdamien.go4lunch.ui.MapView;

import android.content.pm.PackageManager;
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

import com.delombaertdamien.go4lunch.MainActivity;
import com.delombaertdamien.go4lunch.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class MapViewFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map_view, container, false);
        configureToolbar(root);
        initPlaces();
        return root;
    }

    private void initPlaces() {
        //init SDK
        // TODO - CHANGE LOCATION API KEY
        Places.initialize(getActivity().getApplicationContext(), String.valueOf(R.string.apiKeyGooglePlace));

        PlacesClient placesClient = Places.createClient(getContext());
        
    }

    private void configureToolbar (View root){
        MainActivity activity = ((MainActivity)root.getContext());
        activity.getSupportActionBar().setTitle("I'm Hungry!");
    }
}