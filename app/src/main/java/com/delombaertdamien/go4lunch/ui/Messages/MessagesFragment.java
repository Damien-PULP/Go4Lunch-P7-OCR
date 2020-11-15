package com.delombaertdamien.go4lunch.ui.Messages;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.delombaertdamien.go4lunch.ui.activity.ChatActivity;
import com.delombaertdamien.go4lunch.ui.activity.ChatViewModel;
import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.models.Message;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.service.MessageHelper;
import com.delombaertdamien.go4lunch.ui.adapter.AdaptorListViewMessages;
import com.delombaertdamien.go4lunch.utils.FirestoreCall;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.Query;

import java.util.Date;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class MessagesFragment extends Fragment implements AdaptorListViewMessages.Listener, FirestoreCall.CallbackFirestoreUsersOfDiscussion {

    // UI
    private ImageView iconToolbar;
    private TextView nameToolbar;

    private LinearLayout noMessageLayout;
    private RecyclerView recyclerView;
    private TextInputLayout textInputSender;
    // ADAPTER
    private AdaptorListViewMessages adapter;
    // USER OF THIS DISCUSSION
    private Users speakerUser;
    private Users currentUser;
    // ID DISCUSSION
    private String currentUserID;
    private String currentChatID;
    // VIEW MODEL
    private ChatViewModel chatViewModel;
    // VIEW
    private View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_chat_messages, container, false);

        configureViewModel();
        configureUI();
        getUsersOfDiscussion();
        return root;
    }

    // Configuring the view model to using
    private void configureViewModel() {
        this.chatViewModel = ((ChatActivity) getActivity()).getViewModel();
    }
    // Get users of this discussion
    private void getUsersOfDiscussion() {
        currentUserID = chatViewModel.getCurrentUser();
        String speakerUserID = chatViewModel.getSpeakerUser();

        if(currentUserID.compareTo(speakerUserID) > 0){
            //CurrentUser is first
            currentChatID = currentUserID + "&" + speakerUserID;
        }else{
            //SpeakerUser is first
            currentChatID = speakerUserID + "&" + currentUserID;
        }

        FirestoreCall.getUsersOfDiscussionByID(this, currentUserID, speakerUserID);
    }
    // Init UI
    private void configureUI() {

        this.textInputSender = root.findViewById(R.id.fragment_chat_messages_text_input);
        this.noMessageLayout = root.findViewById(R.id.fragment_chat_messages_no_messages);
        this.noMessageLayout.setVisibility(View.VISIBLE);
        this.iconToolbar = root.findViewById(R.id.fragment_chat_messages_toolbar_icon);
        this.nameToolbar = root.findViewById(R.id.fragment_chat_messages_toolbar_name);
        recyclerView = root.findViewById(R.id.fragment_chat_messages_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

    }

    // Configure information discussion into toolbar
    private void configureToolbar() {
        if(speakerUser.getUrlPicture() != null){
            Glide.with(getContext())
                    .load(speakerUser.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(iconToolbar);
        }
        nameToolbar.setText(speakerUser.getUsername());
    }

    // Set UI with information of discussion
    private void setDiscussions(){
        adapter = new AdaptorListViewMessages(generateOptionsForAdapter(MessageHelper.getAllMessagesForChat(currentChatID)), currentUser, speakerUser, this, root);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(adapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getItemCount());
        this.textInputSender.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textInputSender.getEditText().getText().equals("")){
                    Date date = new Date();

                    String nameUser1 = currentUser.getUsername();
                    String idUser1 = currentUser.getUserId();
                    String urlIcon1 = currentUser.getUrlPicture();

                    String nameUser2 = speakerUser.getUsername();
                    String idUser2 = speakerUser.getUserId();
                    String urlIcon2 = speakerUser.getUrlPicture();

                    MessageHelper.createMessageForChat(textInputSender.getEditText().getText().toString(), date, currentChatID, currentUserID, nameUser1, idUser1, urlIcon1, nameUser2, idUser2, urlIcon2);
                    Log.d("MessageFragment", "size" + adapter.getItemCount());
                    textInputSender.getEditText().setText("");
                    hideKeyboardFrom(getActivity(), getView());
                }
            }
        });

        this.onDataSetChanged();
    }

    // Configure data of adapter discussion
    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();
    }

    @Override
    public void onDataSetChanged() {
        noMessageLayout.setVisibility(this.adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        recyclerView.scrollToPosition(adapter.getItemCount());
    }

    // Response of Method 'getUsersOfDiscussion' - call 'setDiscussions'
    @Override
    public void onSuccessGetUsersOfTheDiscussion(Users uid1, Users uid2) {
        if(currentUserID.equals(uid1.getUserId())){
            currentUser = uid1;
            speakerUser = uid2;
        }else{
            currentUser = uid2;
            speakerUser = uid1;
        }
        this.configureToolbar();
        setDiscussions();
    }
    @Override
    public void onFailureGetUsersOfTheDiscussion(Exception e) {
        Log.e("MessagesFragment",  "Error : " + e.getMessage());
    }

    // Method to hide keyboard
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
