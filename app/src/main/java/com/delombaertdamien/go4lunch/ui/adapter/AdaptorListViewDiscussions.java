package com.delombaertdamien.go4lunch.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.delombaertdamien.go4lunch.ChatActivity;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.models.Discussion;

import java.util.ArrayList;
import java.util.List;

public class AdaptorListViewDiscussions  extends RecyclerView.Adapter<AdaptorListViewDiscussions.viewHolderDiscussion>{


    private List<Discussion> discussions = new ArrayList<>();
    private String currentID = null;

    @NonNull
    @Override
    public viewHolderDiscussion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_discussion, parent, false);
        return new viewHolderDiscussion(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderDiscussion holder, int position) {
        holder.bind(discussions.get(position), currentID);
    }

    @Override
    public int getItemCount() {
        return discussions.size();
    }

    public void updateData (List<Discussion> discussions, String currentID){
        this.discussions = discussions;
        this.currentID = currentID;
        notifyDataSetChanged();
    }

    protected class viewHolderDiscussion extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView nameSpeaker;
        private TextView lastMessage;
        private ConstraintLayout item;

        public final String EXTRA_ID_USER = "UserIDForChat";

        public viewHolderDiscussion(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item_discussion);
            nameSpeaker = itemView.findViewById(R.id.item_discussion_name);
            lastMessage = itemView.findViewById(R.id.item_discussion_message);
            icon = itemView.findViewById(R.id.item_discussion_icon);
        }

        public void bind (final Discussion discussion, final String currentUserID){
            String name;
            String urlIcon;
            final String id;

            if(!discussion.getIDUserOne().equals(currentUserID)){
                id = discussion.getIDUserOne();
                name = discussion.getNameUserOne();
                urlIcon = discussion.getUrlIconUserOne();
            }else{
                id = discussion.getIDUserTwo();
                name = discussion.getNameUserTwo();
                urlIcon = discussion.getUrlIconUserTwo();
            }

            if(urlIcon != null){
                Glide.with(itemView.getContext())
                        .load(urlIcon)
                        .apply(RequestOptions.circleCropTransform())
                        .into(icon);
            }
            nameSpeaker.setText(name);
            //lastMessage.setText(discussion);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                    intent.putExtra(EXTRA_ID_USER, id);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
