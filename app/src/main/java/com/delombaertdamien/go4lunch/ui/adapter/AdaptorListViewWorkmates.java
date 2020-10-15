package com.delombaertdamien.go4lunch.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.delombaertdamien.go4lunch.ChatActivity;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.models.POJO.ResultDetails;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.utils.PlacesCall;

import java.util.ArrayList;
import java.util.List;

public class AdaptorListViewWorkmates extends RecyclerView.Adapter<AdaptorListViewWorkmates.WorkmatesViewHolder> {

    private List<Users> users = new ArrayList<>();
    private Context context;
    private boolean isUsingInWorkmatesFragment;

    public AdaptorListViewWorkmates(Context context, boolean isUsingInWorkmatesFragment) {
        this.context = context;
        this.isUsingInWorkmatesFragment = isUsingInWorkmatesFragment;
    }

    @Override
    public WorkmatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_workmate, parent, false);
        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkmatesViewHolder holder, int position) {
        holder.bind(context, users.get(position), isUsingInWorkmatesFragment);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateData(List<Users> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    protected class WorkmatesViewHolder extends RecyclerView.ViewHolder implements PlacesCall.GetDetailOfPlaceCallbacks {

        private final LinearLayout item;
        private final ImageView icon;
        private final TextView text;

        private Users user;

        private boolean isUsingInWorkmatesFragment;

        public final String EXTRA_ID_USER = "UserIDForChat";

        public WorkmatesViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.item_workmate_icon);
            text = itemView.findViewById(R.id.item_workmate_text);
            item = itemView.findViewById(R.id.item_workmate_item);

        }

        public void bind(Context context, final Users user, boolean isUsingInWorkmatesFragment) {
            this.user = user;
            this.isUsingInWorkmatesFragment = isUsingInWorkmatesFragment;
            if (user.getUrlPicture() != null) {
                Glide.with(context)
                        .load(user.getUrlPicture())
                        .apply(RequestOptions.circleCropTransform())
                        .into(icon);
            }
            if (user.getLunchPlaceID() != null) {
                PlacesCall.getDetailOfAPlace(this, user.getLunchPlaceID());

            } else {
                text.setText(user.getUsername() + " "+ context.getString(R.string.not_decided_state_workmates));
                text.setEnabled(false);
            }

            this.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra(EXTRA_ID_USER, user.getUserId());
                    view.getContext().startActivity(intent);
                }
            });

        }

        @Override
        public void onResponseGetDetailOfPlace(ResultDetails result) {
            if (isUsingInWorkmatesFragment) {
                text.setText(user.getUsername() + " " + itemView.getResources().getString(R.string.item_is_eating) + " " + result.getResult().getName());
                text.setEnabled(true);
            } else {
                text.setText(user.getUsername() + " " + itemView.getResources().getString(R.string.joined_state_workmates));
                text.setEnabled(true);
            }
        }

        @Override
        public void onFailureGetDetailOfPlace(Throwable t) {
            text.setText(user.getUsername());
            text.setEnabled(false);
        }
    }
}
