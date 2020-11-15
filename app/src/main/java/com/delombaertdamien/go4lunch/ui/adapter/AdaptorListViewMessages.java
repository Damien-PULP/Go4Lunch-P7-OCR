package com.delombaertdamien.go4lunch.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.models.Message;
import com.delombaertdamien.go4lunch.models.Users;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class AdaptorListViewMessages extends FirestoreRecyclerAdapter<Message, AdaptorListViewMessages.viewHolderMessage> {

    private final Users currentUserID;
    private final Users speakerUser;

    private final Listener callback;

    private final View rootView;


    public interface Listener {
        void onDataSetChanged ();
    }

    public AdaptorListViewMessages(@NonNull FirestoreRecyclerOptions<Message> options, Users currentUserID, Users speakerUser, Listener callback, View root) {
        super(options);
        this.currentUserID = currentUserID;
        this.speakerUser = speakerUser;
        this.callback = callback;
        this.rootView = root;
    }

    @Override
    public viewHolderMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolderMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false), rootView);
    }
    @Override
    protected void onBindViewHolder(viewHolderMessage holder, int position, @NonNull Message model) {
        holder.bind(model, currentUserID, speakerUser);
    }
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        callback.onDataSetChanged();
    }


    public static class viewHolderMessage extends RecyclerView.ViewHolder{

        private final RelativeLayout rootView;
        private final RelativeLayout messagesContainer;
        private final LinearLayout profileContainer;
        private final ImageView iconSender;
        private final ImageView iconSpeaker;
        private final CardView item;
        private final TextView messageText;
        private final TextView dateCreateMessageText;

        public viewHolderMessage(@NonNull View itemView, View root) {
            super(itemView);
            rootView = root.findViewById(R.id.fragment_chat_messages_relative_layout);
            messagesContainer = itemView.findViewById(R.id.fragment_chat_messages_messages_container);
            profileContainer = itemView.findViewById(R.id.fragment_chat_messages_container_profile);
            iconSender = itemView.findViewById(R.id.item_message_icon_sender);
            iconSpeaker = itemView.findViewById(R.id.item_message_icon_speaker);
            item = itemView.findViewById(R.id.item_message_item_card);
            messageText = itemView.findViewById(R.id.item_message_text);
            dateCreateMessageText = itemView.findViewById(R.id.item_message_date_create_text);
        }

        public void bind (Message message, Users currentUser, Users speakerUser){

            boolean isSender;
            messageText.setText(message.getMessage());
            dateCreateMessageText.setText(convertDateToHour(message.getDateCreate()));
            if(!currentUser.getUserId().equals(message.getUserID())){
                item.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorBlank));
                messageText.setTextColor(Color.BLACK);
                if(speakerUser.getUrlPicture() != null){
                    Glide.with(rootView)
                            .load(speakerUser.getUrlPicture())
                            .apply(RequestOptions.circleCropTransform())
                            .into(iconSender);
                    iconSpeaker.setVisibility(View.GONE);
                    iconSpeaker.setEnabled(false);
                }
                isSender = true;
            }else{
                item.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorAccent));
                messageText.setTextColor(Color.WHITE);
                if(currentUser.getUrlPicture() != null){
                    Glide.with(rootView)
                            .load(currentUser.getUrlPicture())
                            .apply(RequestOptions.circleCropTransform())
                            .into(iconSpeaker);
                    iconSender.setVisibility(View.GONE);
                    iconSender.setEnabled(false);
                }
                isSender = false;
            }

            this.updateDesignDependingUser(isSender);
        }
        private void updateDesignDependingUser(Boolean isSender){
            // PROFILE CONTAINER
            RelativeLayout.LayoutParams paramsLayoutHeader = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsLayoutHeader.addRule(isSender ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
            this.profileContainer.setLayoutParams(paramsLayoutHeader);

            // MESSAGE CONTAINER
            RelativeLayout.LayoutParams paramsLayoutContent = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsLayoutContent.addRule(isSender ? RelativeLayout.LEFT_OF : RelativeLayout.RIGHT_OF, R.id.fragment_chat_messages_container_profile);
            this.messagesContainer.setLayoutParams(paramsLayoutContent);

            // CARDVIEW IMAGE SEND
            RelativeLayout.LayoutParams paramsImageView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsImageView.addRule(isSender ? RelativeLayout.ALIGN_RIGHT : RelativeLayout.ALIGN_LEFT, R.id.fragment_chat_messages_messages_container);
            this.item.setLayoutParams(paramsImageView);
            this.rootView.requestLayout();
        }

        private String convertDateToHour(Date date){
            DateFormat dfTime = new SimpleDateFormat("HH:mm");
            return dfTime.format(date);
        }

    }

}
