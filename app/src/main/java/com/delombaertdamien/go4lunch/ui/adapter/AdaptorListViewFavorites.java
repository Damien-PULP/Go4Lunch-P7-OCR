package com.delombaertdamien.go4lunch.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.delombaertdamien.go4lunch.ui.activity.DetailsActivity;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.models.Favorite;
import com.delombaertdamien.go4lunch.service.FavoriteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class AdaptorListViewFavorites extends RecyclerView.Adapter<AdaptorListViewFavorites.viewHolderFavorite> {

    private List<Favorite> favorites = new ArrayList<>();
    private final String uid;


    public AdaptorListViewFavorites(String uid) {
        this.uid = uid;
    }

    @Override
    public viewHolderFavorite onCreateViewHolder( ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_favorite, parent, false);
        return new viewHolderFavorite(view, uid);
    }
    @Override
    public void onBindViewHolder(viewHolderFavorite holder, int position) {
        holder.bind(favorites.get(position));
    }
    @Override
    public int getItemCount() {
        return favorites.size();
    }
    // To update data of this adapter
    public void updateData (List<Favorite> favorites){
        this.favorites = favorites;
        notifyDataSetChanged();
    }


    protected class viewHolderFavorite extends RecyclerView.ViewHolder {

        private final ImageView icon;
        private final TextView name;
        private final LinearLayout item;
        private final ImageButton buttonFavorite;

        private final String EXTRA_NAME = "placeID";
        private final String uid;

        public viewHolderFavorite(@NonNull View itemView, String uid) {
            super(itemView);
            icon = itemView.findViewById(R.id.item_favorite_icon);
            name = itemView.findViewById(R.id.item_favorite_name_place);
            item = itemView.findViewById(R.id.item_favorite_item);
            buttonFavorite = itemView.findViewById(R.id.item_favorite_button_favorite);
            this.uid = uid;
        }

        public void bind (final Favorite favorite){
            if(favorite.getUrlPlace() != null){
                Glide.with(itemView)
                        .load(favorite.getUrlPlace())
                        .apply(RequestOptions.circleCropTransform())
                        .into(icon);
            }
            name.setText(favorite.getNamePlace());
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), DetailsActivity.class);
                    intent.putExtra(EXTRA_NAME, favorite.getIdPlace());
                    itemView.getContext().startActivity(intent);
                }
            });
            buttonFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FavoriteHelper.deleteFavoritePlace(uid, favorite.getIdPlace());
                    notifyDataSetChanged();
                }
            });
        }

    }

}
