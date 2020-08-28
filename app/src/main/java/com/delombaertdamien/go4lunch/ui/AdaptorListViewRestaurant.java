package com.delombaertdamien.go4lunch.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.delombaertdamien.go4lunch.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdaptorListViewRestaurant extends RecyclerView.Adapter<RestaurantViewHolder> {

    private List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9);

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return list.size();
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
