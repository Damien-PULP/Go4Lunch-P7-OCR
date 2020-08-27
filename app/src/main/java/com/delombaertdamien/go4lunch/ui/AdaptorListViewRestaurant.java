package com.delombaertdamien.go4lunch.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.delombaertdamien.go4lunch.R;

public class AdaptorListViewRestaurant extends RecyclerView.Adapter<RestaurantViewHolder> {

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

 class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private final ImageView icon;
    private final TextView name;
    private final TextView information;
    private final TextView hour;
    private final TextView distance;
    private final TextView numberWorkmates;

     public RestaurantViewHolder(View itemView) {
         super(itemView);

         icon = itemView.findViewById(R.id.item_restaurant_icon);
         name = itemView.findViewById(R.id.item_restaurant_name);
         information = itemView.findViewById(R.id.item_restaurant_information);
         hour = itemView.findViewById(R.id.item_restaurant_open_hour);
         distance = itemView.findViewById(R.id.item_restaurant_distance);
         numberWorkmates = itemView.findViewById(R.id.item_restaurant_nb_workmate);
     }

     public void bind (){

     }
 }
